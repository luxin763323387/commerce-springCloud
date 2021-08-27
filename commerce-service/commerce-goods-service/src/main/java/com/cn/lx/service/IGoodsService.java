package com.cn.lx.service;

import com.cn.lx.common.TableId;
import com.cn.lx.goods.GoodsInfo;
import com.cn.lx.goods.SimpleGoodsInfo;
import com.cn.lx.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * @author StevenLu
 * @date 2021/8/27 下午9:21
 *
 */
public interface IGoodsService{

    /**
     * <h2>根据 TableId 查询商品详细信息</h2>
     * */
    List<GoodsInfo> getGoodsInfoByTableId(TableId tableId);

    /**
     * <h2>获取分页的商品信息</h2>
     * */
    PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page);

    /**
     * <h2>根据 TableId 查询简单商品信息</h2>
     * */
    List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId);

//    /**
//     * <h2>扣减商品库存</h2>
//     * */
//    Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories);

}
