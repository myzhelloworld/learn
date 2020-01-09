package com.pinyougou.shop.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zx
 * @version 1.0
 * @date 2020/1/3  16:33
 */
public class UserDetailServiceImpl implements UserDetailsService {
    @Reference
    private SellerService sellerService;
    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        System.out.println("进入loadUserByUsername");
        List<GrantedAuthority> list=new ArrayList <>();
        TbSeller seller = sellerService.findOne(username);
        if(seller!=null && seller.getStatus().equals("1")){
            list.add(new SimpleGrantedAuthority("ROLE_SELLER"));
            return new User(username,seller.getPassword(),list);
        }
        else {
            return null;
        }
    }
}
