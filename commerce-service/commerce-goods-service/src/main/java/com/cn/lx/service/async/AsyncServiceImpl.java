package com.cn.lx.service.async;

import com.alibaba.fastjson.JSON;
import com.cn.lx.dao.EcommerceGoodsDao;
import com.cn.lx.entity.EcommerceGoods;
import com.cn.lx.enums.GoodsConstant;
import com.cn.lx.goods.GoodsInfo;
import com.cn.lx.goods.SimpleGoodsInfo;
import io.jsonwebtoken.lang.Collections;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author StevenLu
 * @date 2021/8/27 下午9:29
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AsyncServiceImpl implements IAsyncService {

    private final StringRedisTemplate redisTemplate;

    private final EcommerceGoodsDao ecommerceGoodsDao;

    public AsyncServiceImpl(EcommerceGoodsDao ecommerceGoodsDao, StringRedisTemplate redisTemplate) {
        this.ecommerceGoodsDao = ecommerceGoodsDao;
        this.redisTemplate = redisTemplate;
    }

    @Async("getAsyncExecutor")
    @Override
    public void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId) {

        log.info("async task running taskId: [{}]", taskId);
        StopWatch watch = StopWatch.createStarted();

        boolean isIllegal = false;

        Set<String> goodInfoSet = new HashSet<>();
        List<EcommerceGoods> ecommerceGoodsList = new ArrayList<>();

        for (GoodsInfo e : goodsInfos) {
            // 基本条件不满足的, 直接过滤器
            if (e.getPrice() <= 0 || e.getSupply() <= 0) {
                log.info("goods info is invalid: [{}]", JSON.toJSONString(e));
                continue;
            }

            String format = String.format("%s,%s,%s",
                    e.getGoodsCategory(),
                    e.getBrandCategory(),
                    e.getGoodsName());

            if (goodInfoSet.contains(format)) {
                isIllegal = true;
            }

            goodInfoSet.add(format);
            ecommerceGoodsList.add(EcommerceGoods.to(e));
        }

        if (isIllegal && Collections.isEmpty(ecommerceGoodsList)) {
            watch.stop();
            log.warn("import nothing: [{}]", JSON.toJSONString(ecommerceGoodsList));
            log.info("check and import goods done: [{}ms]",
                    watch.getTime(TimeUnit.MILLISECONDS));
            return;
        }

        List<EcommerceGoods> savedGoods = ecommerceGoodsList.stream()
                .filter(this::validateGoodsIsExist)
                .collect(Collectors.toList());
        ecommerceGoodsDao.saveAll(savedGoods);

        // 将入库商品信息同步到 Redis 中
        saveNewGoodsInfoToRedis(savedGoods);

        log.info("save goods info to db and redis: [{}]", savedGoods.size());

        watch.stop();
        log.info("check and import goods success: [{}ms]",
                watch.getTime(TimeUnit.MILLISECONDS));

    }

    private boolean validateGoodsIsExist(EcommerceGoods e) {
        EcommerceGoods ecommerceGoods =
                ecommerceGoodsDao.findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(e.getGoodsCategory(),
                        e.getBrandCategory(), e.getGoodsName()).orElse(null);
        return Objects.nonNull(ecommerceGoods);
    }

    /**
     * <h2>将保存到数据表中的数据缓存到 Redis 中</h2>
     * dict: key -> <id, SimpleGoodsInfo(json)>
     */
    private void saveNewGoodsInfoToRedis(List<EcommerceGoods> savedGoods) {

        // 由于 Redis 是内存存储, 只存储简单商品信息
        List<SimpleGoodsInfo> simpleGoodsInfos = savedGoods.stream()
                .map(EcommerceGoods::toSimple)
                .collect(Collectors.toList());

        Map<String, String> id2JsonObject = new HashMap<>(simpleGoodsInfos.size());
        simpleGoodsInfos.forEach(
                g -> id2JsonObject.put(g.getId().toString(), JSON.toJSONString(g))
        );

        // 保存到 Redis 中
        redisTemplate.opsForHash().putAll(
                GoodsConstant.ECOMMERCE_GOODS_DICT_KEY,
                id2JsonObject
        );
    }
}
