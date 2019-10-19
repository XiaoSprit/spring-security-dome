package org.sprit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;

/**
 *
 * @author: Administrator
 * @date: 2019/10/18 12:12
 */
@EnableWebSecurity
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class,args);
    }

    @Bean("sessionStrategy")
    public SessionStrategy sessionStrategy() {
        return new HttpSessionSessionStrategy();
    }
}
