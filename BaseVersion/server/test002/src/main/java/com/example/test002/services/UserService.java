package com.example.test002.services;

import com.example.test002.mapper.UserMapper;
import com.example.test002.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public int addUser(User user){
        return userMapper.addUser(user);
    }

    public int deleteUserByUsername(String username){
        return userMapper.deleteUserByUsername(username);
    }

    /**
     * 用户修改密码时使用
     * @param user
     * @return
     */
    public int updateUser(User user){
        return userMapper.updateUser(user);
    }

    /**
     * 暂用于根据用户名获取用户密码，校验用
     * 之所以不直接返回String类型的密码
     * 一是安全考虑，二是方便以后扩展
     * @param username
     * @return
     */
    public User getUserByUsername(String username){
        return userMapper.getUserByUsername(username);
    }

    /**
     * 用户登陆时使用，用于检查用户密码是否输入正确
     * @param user
     * @return
     */
    public boolean checkUser(User user){
        String passA = getUserByUsername(user.getUsername()).getPassword();
        String passB = user.getPassword();
        if(passA.equals(passB)){
            return true;
        }
        return false;
//        return user.getPassword().equals(userMapper.getUserByUsername(user.getUsername()));
    }
}
