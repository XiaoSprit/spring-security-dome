package org.sprit.security.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.sprit.entity.AccountUser;


/**
 * @author: Administrator
 * @date: 2019/10/18 15:04
 */
@Configuration
public class AccountUserDetailService implements UserDetailsService {

    private PasswordEncoder passwordEncoder;

    public AccountUserDetailService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountUser user = new AccountUser();
        user.setUserName(username);
        user.setPassword(passwordEncoder.encode("123456"));
        //输出加密后的密码
        System.out.println("---------"+user.getPassword());

        return new User(username,user.getPassword(),user.getEnable(),
                user.getAccountNonExpired(),
                user.getCredentialsNonExpired(),
                user.getAccountNonLocked(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin")
                );
    }
}
