package com.cn.lx.service.impl;

import com.cn.lx.common.TableId;
import com.cn.lx.goods.GoodsInfo;
import com.cn.lx.goods.SimpleGoodsInfo;
import com.cn.lx.service.IGoodsService;
import com.cn.lx.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * @author StevenLu
 * @date 2021/8/27 下午9:23
 */
public class IGoodsServiceImpl implements IGoodsService {
    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {
        return null;
    }

    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {
        return null;
    }

    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {
        return null;
    }
}
