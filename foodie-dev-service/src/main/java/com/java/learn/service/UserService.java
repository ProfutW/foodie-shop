package com.java.learn.service;

import com.java.learn.pojo.Users;
import com.java.learn.pojo.bo.UserBO;

public interface UserService {
    boolean queryUsernameIsExist(String username);

    Users checkPasswordByUsername(String username, String password) throws Exception;

    Users createUser(UserBO userBo) throws Exception;
}
