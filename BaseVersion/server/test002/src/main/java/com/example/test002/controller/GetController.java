package com.example.test002.controller;


import com.example.test002.model.User;
import com.example.test002.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
@RestController
public class GetController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public String test(){
        User user = new User("zjc","8989");
        userService.addUser(user);
        return "fuck off";
    }

    /**
     * 添加用户操作
     * 必要步骤：
     * 1. 查重，检查是否已存在重名用户
     * 2. 若不存在，则添加此用户，并返回
     * 3.若存在，则直接返回
     * @param username
     * @param password
     * @return
     */

    @ResponseBody
    @RequestMapping(value ="/addUser",method = RequestMethod.GET)
    public int addUser(@RequestParam("username") String username,@RequestParam("password") String password){
        User queryUser = userService.getUserByUsername(username);
        if(queryUser==null){//user表中不存在,插入此用户，返回0
            User user = new User(username,password);
            userService.addUser(user);
            return 0;
        }else {//已存在重名用户
            return 1;
        }
    }

    @ResponseBody
    @RequestMapping(value ="/loginCheck",method = RequestMethod.GET)
    public int loginCheck(@RequestParam("username") String username,@RequestParam("password") String password){
        User queryUser = userService.getUserByUsername(username);
        if(queryUser==null){//user表中不存在此用户，登陆失败，错误类型属于此用户不存在
            return 2;
        }else {//此用户存在
            if(queryUser.getPassword().equals(password)){//通过密码检验，成功登陆
                return 0;
            }else {//未通过密码检验
                return 1;
            }
        }
    }

    @ResponseBody
    @RequestMapping(value ="/sign",method = RequestMethod.GET)
    public String sign(@RequestParam("username") String username,@RequestParam("password") String password){
        User queryUser = userService.getUserByUsername(username);
        if(queryUser==null){//user表中不存在此用户，登陆失败，错误类型属于此用户不存在
            return "don't exist";
        }else {//此用户存在
            User user = new User(username,password);
            if(userService.checkUser(user)){//通过密码检验，成功登陆
                return "success";
            }else {//未通过密码检验
                return userService.getUserByUsername(username).getPassword();
            }
        }
    }



    /**
     * 更改密码，需保证用户在登陆状态，必要步骤如下：
     * 1. 根据username检查此用户是否存在
     * 2. 若存在，则修改密码，随后返回
     * 3. 若不存在，直接返回
     * @return true代表操作成功
     */
    @ResponseBody
    @RequestMapping(value ="/updateUserPass",method = RequestMethod.GET)
    public int updateUserPass(@RequestParam("username") String username,@RequestParam("password") String password){
        User queryUser = userService.getUserByUsername(username);
        if(queryUser!=null){//user表中存在此用户
            User user = new User(username,password);
            userService.updateUser(user);
            return 0;
        }else {//若不存在
            return 1;
        }
    }

    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(){
        return "hello";
    }

}
