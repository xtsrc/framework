package com.xt.framewrok.auth.service;

import com.xt.framewrok.auth.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author tao.xiong
 * @Description 用户凭据校验
 * @Date 2022/7/15 14:59
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO 实际开发中，这里请修改从数据库中查询...
        LoginUser user = new LoginUser();
        user.setUserName(username);
        // 密码为 123456 ，且加密
        user.setPassword(passwordEncoder.encode("123456"));
        return user;
    }
}
