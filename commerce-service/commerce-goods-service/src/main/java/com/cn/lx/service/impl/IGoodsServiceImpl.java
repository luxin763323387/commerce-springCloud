package com.cn.lx.service.impl;

import com.alibaba.fastjson.JSON;
import com.cn.lx.common.TableId;
import com.cn.lx.dao.EcommerceGoodsDao;
import com.cn.lx.entity.EcommerceGoods;
import com.cn.lx.enums.GoodsConstant;
import com.cn.lx.goods.DeductGoodsInventory;
import com.cn.lx.goods.GoodsInfo;
import com.cn.lx.goods.SimpleGoodsInfo;
import com.cn.lx.service.IGoodsService;
import com.cn.lx.vo.PageSimpleGoodsInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author StevenLu
 * @date 2021/8/27 下午9:23
 */
@Slf4j
@Service
public class IGoodsServiceImpl implements IGoodsService {

    private final StringRedisTemplate stringRedisTemplate;

    private final EcommerceGoodsDao ecommerceGoodsDao;

    public IGoodsServiceImpl(StringRedisTemplate stringRedisTemplate, EcommerceGoodsDao ecommerceGoodsDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.ecommerceGoodsDao = ecommerceGoodsDao;
    }

    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {

        // 详细的商品信息, 不能从 redis cache 中去拿
        List<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId)
                .collect(Collectors.toList());
        log.info("get goods info by ids: [{}]", JSON.toJSONString(ids));

        List<GoodsInfo> goodsInfoList = IterableUtils.toList(ecommerceGoodsDao.findAllById(ids)).stream().map(EcommerceGoods::toGoodsInfo).collect(Collectors.toList());
        return goodsInfoList;
    }

    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {
        // 分页不能从 redis cache 中去拿
        if (page <= 1) {
            page = 1;   // 默认是第一页
        }

        // 这里分页的规则(你可以自由修改): 1页10调数据, 按照 id 倒序排列
        Pageable pageable = PageRequest.of(
                page - 1, 10, Sort.by("id").descending()
        );
        Page<EcommerceGoods> orderPage = ecommerceGoodsDao.findAll(pageable);

        // 是否还有更多页: 总页数是否大于当前给定的页
        boolean hasMore = orderPage.getTotalPages() > page;

        return new PageSimpleGoodsInfo(
                orderPage.getContent().stream()
                        .map(EcommerceGoods::toSimple).collect(Collectors.toList()),
                hasMore
        );
    }

    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {

        List<Object> goodIds = tableId.getIds().stream()
                .map(e -> e.getId().toString())
                .collect(Collectors.toList());

        List<Object> cachedSimpleGoodsInfos = stringRedisTemplate
                .opsForHash().multiGet(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, goodIds)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        //缓存不为空
        if (CollectionUtils.isNotEmpty(cachedSimpleGoodsInfos)) {
            if (cachedSimpleGoodsInfos.size() == goodIds.size()) {
                return cachedSimpleGoodsInfos.stream()
                        .map(SimpleGoodsInfo::object2SimpleGoodsInfo)
                        .collect(Collectors.toList());
            } else {
                //不相等有部分数据需要从数据库获取
                List<Long> redisGoodsIds = new ArrayList<>();
                List<SimpleGoodsInfo> redisSimpleGoodsInfos = new ArrayList<>();
                cachedSimpleGoodsInfos.forEach(e -> {
                    redisGoodsIds.add(SimpleGoodsInfo.object2Id(e));
                    redisSimpleGoodsInfos.add(SimpleGoodsInfo.object2SimpleGoodsInfo(e));
                });

                List<TableId.Id> ids = CollectionUtils.subtract(goodIds, redisGoodsIds).stream()
                        .map(e -> new TableId.Id((Long) e))
                        .collect(Collectors.toList());

                TableId dbTableId = new TableId(ids);
                List<SimpleGoodsInfo> dbSimpleGoodsInfos = queryGoodsFromDBAndCacheToRedis(dbTableId);
                //返回db和redis数据并集
                return new ArrayList<>(CollectionUtils.union(redisSimpleGoodsInfos, dbSimpleGoodsInfos));
            }
        }

        return queryGoodsFromDBAndCacheToRedis(tableId);
    }

    /**
     * <h2>从数据表中查询数据, 并缓存到 Redis 中</h2>
     */
    private List<SimpleGoodsInfo> queryGoodsFromDBAndCacheToRedis(TableId tableId) {

        // 从数据表中查询数据并做转换
        List<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId).collect(Collectors.toList());
        log.info("get simple goods info by ids (from db): [{}]",
                JSON.toJSONString(ids));
        List<EcommerceGoods> ecommerceGoods = IterableUtils.toList(
                ecommerceGoodsDao.findAllById(ids)
        );
        List<SimpleGoodsInfo> result = ecommerceGoods.stream()
                .map(EcommerceGoods::toSimple).collect(Collectors.toList());
        // 将结果缓存, 下一次可以直接从 redis cache 中查询
        log.info("cache goods info: [{}]", JSON.toJSONString(ids));

        Map<String, String> id2JsonObject = new HashMap<>(result.size());
        result.forEach(g -> id2JsonObject.put(
                g.getId().toString(), JSON.toJSONString(g)
        ));
        // 保存到 Redis 中
        stringRedisTemplate.opsForHash().putAll(
                GoodsConstant.ECOMMERCE_GOODS_DICT_KEY, id2JsonObject);
        return result;
    }

    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {

        List<Long> goodsIds = new ArrayList<>();
        deductGoodsInventories.forEach(e -> {
            if (e.getCount() <= 0) {
                throw new RuntimeException("扣减库存数量必须大于0");
            }
            goodsIds.add(e.getGoodsId());
        });
        List<EcommerceGoods> ecommerceGoods = IterableUtils.toList(
                ecommerceGoodsDao.findAllById(goodsIds));


        // 根据传递的 goodsIds 查询不到商品对象, 抛异常
        if (CollectionUtils.isEmpty(ecommerceGoods)) {
            throw new RuntimeException("can not found any goods by request");
        }
        // 查询出来的商品数量与传递的不一致, 抛异常
        if (ecommerceGoods.size() != deductGoodsInventories.size()) {
            throw new RuntimeException("request is not valid");
        }

        // goodsId -> DeductGoodsInventory
        Map<Long, DeductGoodsInventory> goodsId2Inventory =
                deductGoodsInventories.stream().collect(
                        Collectors.toMap(DeductGoodsInventory::getGoodsId,
                                Function.identity())
                );

        // 检查是不是可以扣减库存, 再去扣减库存
        ecommerceGoods.forEach(g -> {
            Long currentInventory = g.getInventory();
            Integer needDeductInventory = goodsId2Inventory.get(g.getId()).getCount();
            if (currentInventory < needDeductInventory) {
                log.error("goods inventory is not enough: [{}], [{}]",
                        currentInventory, needDeductInventory);
                throw new RuntimeException("goods inventory is not enough: " + g.getId());
            }

            // 扣减库存
            g.setInventory(currentInventory - needDeductInventory);
            log.info("deduct goods inventory: [{}], [{}], [{}]", g.getId(),
                    currentInventory, g.getInventory());
        });

        ecommerceGoodsDao.saveAll(ecommerceGoods);
        log.info("deduct goods inventory done");

        return true;
    }
}
