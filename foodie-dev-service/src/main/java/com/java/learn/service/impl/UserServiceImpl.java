package com.java.learn.service.impl;

import com.java.learn.enums.Sex;
import com.java.learn.mapper.UsersMapper;
import org.n3r.idworker.Sid;
import com.java.learn.pojo.Users;
import com.java.learn.pojo.bo.UserBO;
import com.java.learn.service.UserService;
import com.java.learn.utils.DateUtil;
import com.java.learn.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    @Value("${project.config.user.birthday}")
    private String defaultBirth;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Example userExample = new Example(Users.class);
        userExample.selectProperties("id")
                .and()
                .andEqualTo("username", username);

        Users result = usersMapper.selectOneByExample(userExample);

        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBo) throws Exception {
        Users user = new Users();

        user.setId(sid.nextShort());

        user.setUsername(userBo.getUsername());

        user.setPassword(MD5Utils.getMD5Str(userBo.getPassword()));

        // 默认用户昵称同用户名
        user.setNickname(userBo.getUsername());
        user.setBirthday(DateUtil.stringToDate(customconfi));
        user.setSex(Sex.secret.type);

        user.setFace("");

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users checkPasswordByUsername (String username, String password) throws Exception {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria =  userExample.createCriteria();
        userCriteria.andEqualTo("username", username)
                .andEqualTo("password", MD5Utils.getMD5Str(password));

        Users result = usersMapper.selectOneByExample(userExample);

        return result;
    }
}
