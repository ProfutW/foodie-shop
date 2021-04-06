package com.java.learn.service.impl;

import com.java.learn.enums.YesOrNo;
import com.java.learn.mapper.UserAddressMapper;
import com.java.learn.pojo.UserAddress;
import com.java.learn.pojo.bo.AddressBO;
import com.java.learn.service.UserAddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> getAddressByUserId(String userId) {
        Example example = new Example(UserAddress.class);
        example.and().andEqualTo("userId", userId);

        return userAddressMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addUserAddress(AddressBO address) {
        UserAddress userAddress = new UserAddress();

        // 若用户之前没有地址，新增地址设为默认地址
        Integer isDefault = YesOrNo.NO.type;
        List<UserAddress> userAddresses = this.getAddressByUserId(address.getUserId());
        if (userAddresses == null || userAddresses.isEmpty()) {
            isDefault = YesOrNo.YES.type;
        }

        String addressId = sid.nextShort();
        BeanUtils.copyProperties(address, userAddress);

        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO address) {
        String addressId = address.getAddressId();

        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(address, userAddress);

        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {

        Example example = new Example(UserAddress.class);
        example.and().andEqualTo("userId", userId);
        example.and().andEqualTo("id", addressId);

        userAddressMapper.deleteByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setDefaultAddress(String userId, String addressId) {
        Example example = new Example(UserAddress.class);
        String[] selectProps = new String[] {"id"};
        example.selectProperties(selectProps).and().andEqualTo("isDefault", YesOrNo.YES.type);

        UserAddress userAddress = userAddressMapper.selectOneByExample(example);
        if (userAddress != null && userAddress.getId() == addressId) {
            return;
        }

        if (userAddress != null) {
            // 将原来的address更新为非默认地址
            userAddress.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(userAddress);
        }

        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setUserId(userId);
        defaultAddress.setId(addressId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);

        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress getUserAddress(String userId, String addressId) {
        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setId(addressId);

        return userAddressMapper.selectOne(address);
    }
}
