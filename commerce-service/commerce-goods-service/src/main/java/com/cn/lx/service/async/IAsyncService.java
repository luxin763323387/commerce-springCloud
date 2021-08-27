package com.cn.lx.service.async;

import com.cn.lx.goods.GoodsInfo;

import java.util.List;

/**
 * <h1>异步服务接口定义</h1>
 * @author StevenLu
 * */
public interface IAsyncService {

    /**
     * 异步将商品信息保存下来
     * @param goodsInfos
     * @param taskId
     */
    void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId);
}
