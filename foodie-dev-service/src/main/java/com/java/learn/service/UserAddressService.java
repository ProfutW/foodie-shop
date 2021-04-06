package com.java.learn.service;


import com.java.learn.pojo.UserAddress;
import com.java.learn.pojo.bo.AddressBO;

import java.util.List;

public interface UserAddressService {
    List<UserAddress> getAddressByUserId(String userId);

    void addUserAddress(AddressBO address);

    void updateUserAddress(AddressBO address);

    void deleteUserAddress(String userId, String addressId);

    void setDefaultAddress(String userId, String addressId);

    UserAddress getUserAddress(String userId, String addressId);

}
