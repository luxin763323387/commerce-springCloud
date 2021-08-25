package com.cn.lx.service;

import com.cn.lx.account.BalanceInfo;

/**
 * @author StevenLu
 * @date 2021/8/19 下午11:32
 */
public interface IBalanceService {


    /**
     * <h2>获取当前用户余额信息</h2>
     */
    BalanceInfo getCurrentUserBalanceInfo();

    /**
     * <h2>扣减用户余额</h2>
     *
     * @param balanceInfo 代表想要扣减的余额
     */
    BalanceInfo deductBalance(BalanceInfo balanceInfo);
}
