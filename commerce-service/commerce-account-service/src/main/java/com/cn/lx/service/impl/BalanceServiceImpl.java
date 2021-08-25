package com.cn.lx.service.impl;

import com.cn.lx.account.BalanceInfo;
import com.cn.lx.dao.EcommerceBalanceDao;
import com.cn.lx.entity.EcommerceBalance;
import com.cn.lx.filter.AccessContext;
import com.cn.lx.service.IBalanceService;
import com.cn.lx.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author StevenLu
 * @date 2021/8/23 下午10:02
 */
@Slf4j
@Service
public class BalanceServiceImpl implements IBalanceService {

    private final EcommerceBalanceDao ecommerceBalanceDao;

    public BalanceServiceImpl(EcommerceBalanceDao ecommerceBalanceDao) {
        this.ecommerceBalanceDao = ecommerceBalanceDao;
    }

    @Override
    public BalanceInfo getCurrentUserBalanceInfo() {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        BalanceInfo balanceInfo = new BalanceInfo(loginUserInfo.getUid(), 0L);

        EcommerceBalance ecommerceBalance = ecommerceBalanceDao.findByUserId(loginUserInfo.getUid());
        if (Objects.isNull(ecommerceBalance)) {
            EcommerceBalance newBalance = new EcommerceBalance();
            newBalance.setId(loginUserInfo.getUid());
            newBalance.setBalance(0L);
            ecommerceBalanceDao.save(newBalance);
            return balanceInfo;
        } else {
            balanceInfo.setBalance(ecommerceBalance.getBalance());
            return balanceInfo;
        }
    }

    @Override
    public BalanceInfo deductBalance(BalanceInfo balanceInfo) {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        EcommerceBalance ecommerceBalance = ecommerceBalanceDao.findByUserId(loginUserInfo.getUid());
        if (Objects.isNull(ecommerceBalance) || ecommerceBalance.getBalance() - balanceInfo.getBalance() < 0) {
            throw new RuntimeException("user balance is not enough!");
        }

        Long originalBalance = ecommerceBalance.getBalance();
        ecommerceBalance.setBalance(originalBalance - balanceInfo.getBalance());
        log.info("deduct balance: [{}], [{}], [{}]",
                ecommerceBalanceDao.save(ecommerceBalance).getId(), originalBalance,
                balanceInfo.getBalance());

        return new BalanceInfo(
                ecommerceBalance.getUserId(),
                ecommerceBalance.getBalance());
    }
}
