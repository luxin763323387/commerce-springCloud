package com.cn.lx.service;

import com.cn.lx.account.AddressInfo;
import com.cn.lx.common.TableId;

/**
 * @author StevenLu
 * @date 2021/8/19 下午11:29
 */
public interface IAddressService {

    /**
     * <h2>创建用户地址信息</h2>
     */
    TableId createAddressInfo(AddressInfo addressInfo);

    /**
     * <h2>获取当前登录的用户地址信息</h2>
     */
    AddressInfo getCurrentAddressInfo();

    /**
     * <h2>通过 id 获取用户地址信息, id 是 EcommerceAddress 表的主键</h2>
     */
    AddressInfo getAddressInfoById(Long id);

    /**
     * <h2>通过 TableId 获取用户地址信息</h2>
     */
    AddressInfo getAddressInfoByTableId(TableId tableId);

}
