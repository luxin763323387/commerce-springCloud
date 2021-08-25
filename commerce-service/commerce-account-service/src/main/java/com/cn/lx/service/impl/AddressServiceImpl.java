package com.cn.lx.service.impl;

import com.alibaba.fastjson.JSON;
import com.cn.lx.account.AddressInfo;
import com.cn.lx.common.TableId;
import com.cn.lx.dao.EcommerceAddressDao;
import com.cn.lx.entity.EcommerceAddress;
import com.cn.lx.filter.AccessContext;
import com.cn.lx.service.IAddressService;
import com.cn.lx.vo.LoginUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author StevenLu
 * @date 2021/8/22 下午7:40
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private EcommerceAddressDao ecommerceAddressDao;

    @Override
    public TableId createAddressInfo(AddressInfo addressInfo) {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        List<EcommerceAddress> ecommerceAddressList = addressInfo.getAddressItems().stream()
                .map(e -> EcommerceAddress.to(loginUserInfo.getUid(), e))
                .collect(Collectors.toList());

        List<EcommerceAddress> resultList = ecommerceAddressDao.saveAll(ecommerceAddressList);

        log.info("用户创建地址信息:{}", JSON.toJSONString(resultList));

        List<TableId.Id> ids = resultList.stream().map(e -> {
            TableId.Id id = new TableId.Id();
            id.setId(e.getId());
            return id;
        }).collect(Collectors.toList());
        return new TableId(ids);
    }

    @Override
    public AddressInfo getCurrentAddressInfo() {

        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        List<EcommerceAddress> ecommerceAddressList = ecommerceAddressDao.findAllByUserId(loginUserInfo.getUid());

        List<AddressInfo.AddressItem> addressItemList = ecommerceAddressList.stream().map(EcommerceAddress::toAddressItem).collect(Collectors.toList());
        AddressInfo addressInfo = new AddressInfo(loginUserInfo.getUid(), addressItemList);

        return addressInfo;
    }

    @Override
    public AddressInfo getAddressInfoById(Long id) {

        EcommerceAddress ecommerceAddress = ecommerceAddressDao.findById(id).orElse(null);

        if (Objects.isNull(ecommerceAddress)) {
            return new AddressInfo(-1L, Collections.emptyList());
        }

        return new AddressInfo(AccessContext.getLoginUserInfo().getUid(),
                Collections.singletonList(ecommerceAddress.toAddressItem()));
    }

    @Override
    public AddressInfo getAddressInfoByTableId(TableId tableId) {

        List<Long> ids = tableId.getIds().stream().map(TableId.Id::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return new AddressInfo(-1L, Collections.emptyList());
        }

        List<EcommerceAddress> ecommerceAddressList = ecommerceAddressDao.findAllById(ids);
        List<AddressInfo.AddressItem> addressItemList = ecommerceAddressList.stream().map(EcommerceAddress::toAddressItem).collect(Collectors.toList());
        return new AddressInfo(ecommerceAddressList.get(0).getUserId(), addressItemList);
    }
}
