package org.sprit.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.sprit.security.handler.MyAuthenticationSuccess;

/**
 * 浏览器安全配置
 * @author: Administrator
 * @date: 2019/10/18 12:30
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    private final MyAuthenticationSuccess authenticationSuccess;

    public BrowserSecurityConfig(MyAuthenticationSuccess authenticationSuccess) {
        this.authenticationSuccess = authenticationSuccess;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin() 表单方式
                //授权配置
        http .authorizeRequests()
                //表示跳转到登录页面的请求不被拦截，否则会进入无限循环。
                .antMatchers("/authentication/require","/css/**").permitAll()
                //所有请求
                .anyRequest()
                //多需要认证
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/authentication/require")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccess)
                .and().csrf().disable().cors();

    }
}
