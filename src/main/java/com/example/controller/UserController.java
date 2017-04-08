package com.example.controller;

import com.example.entity.User;
import com.example.validation.annotation.FormIncludeJson;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description:
 * @Author tengguodong
 * @Date 2017/2/17 14:31
 */
@RestController
public class UserController {
    @RequestMapping(value = "/addUser/")
    private String addUser(@Valid User user, BindingResult result) {
        if(result.hasErrors()){
            List<ObjectError> list = result.getAllErrors();
            for(ObjectError error: list){
                System.out.println(error.getDefaultMessage());//验证信息
            }

        }
        return user.getNames().toString();
    }

    @RequestMapping(value = "/saveUser/")
    private User saveUser(@FormIncludeJson User user) {
        return user;
    }
}
