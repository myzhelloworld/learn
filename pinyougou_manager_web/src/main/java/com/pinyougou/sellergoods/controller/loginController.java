package com.pinyougou.sellergoods.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zx
 * @version 1.0
 * @date 2020/1/2  8:55
 */
@RestController
@RequestMapping("login")
public class loginController {

    @RequestMapping("name")
    public Map<String,Object> name(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,Object> map=new HashMap<>();
        map.put("loginName",name);
        return map;
    }

}
