package com.example.test002.mapper;


import com.example.test002.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    /**
     * 用于用户注册，增添用户
     * @param user
     * @return
     */
    int addUser(User user);

    /**
     * 根据用户名删除用户
     * @param username
     * @return
     */
    int deleteUserByUsername(String username);

    /**
     * 根据username获取user的password，用于查询
     * @param username
     * @return
     */
    User getUserByUsername(String username);

    /**
     * 根据user的用户名更新password
     * @param user
     * @return
     */
    int updateUser(User user);
}
