## Spring Security

spring security 是一个基于Spring的一个安全框架 主要包含`认证`和`授权`两大安全模块 spring security可以轻松自定义和扩展以满足各种需求 并对常见的web安全攻击提供了防护支持 如果你的web框架选择的是Spring 那么在安全方面 spring security是一个不错的选择



##  WebSecurityConfigurerAdapter

 创建一个配置类`BrowserSecurityConfig`继承`org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter`这个抽象类并重写`configure(HttpSecurity http)`方法。`WebSecurityConfigurerAdapter`是由Spring Security提供的Web应用安全配置的适配器： 

``` java
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单方式
                .and()
                .authorizeRequests() // 授权配置
                .anyRequest()  // 所有请求
                .authenticated(); // 都需要认证
    }
}
```

## 基本原理

上图基本配置的执行流程

![](https://mrbird.cc/img/QQ%E6%88%AA%E5%9B%BE20180707111356.png)

spring security 包含众多过滤器 这些过滤器形成一条链条，所有的请求必须通过这些过滤器后才能访问到资源。

其中 `UsernmaePasswordAuthenticationFilter` 用于处理基于表单登录的认证，而  `BasicAuthenticationFilter`用于处理Http basic的认证，后面还有一系列别的过滤器（可通过配置开启）。在过滤器末尾还有 `FilterSecurityInterceptor` 的拦截器 ，用于判断前面的请求省份是否认证成功，是否有相应的权限，当身份认证失败或者权限不足时便会抛出相应的异常，`ExceptionTranslateFilter` 捕获处理。所以我们在 `ExceptionTranslateFilter` 过滤器用于处理 `FilterSecurityInterceptor` 拦截器抛出的异常并进行处理 比如需要身份认证时将请求重定向到相应的页面,当认证失败时或权限不足时返回相应的信息。



## 认证成功和失败

Spring security 有一套默认的处理登录成功和失败的方法

**自定义登录**

要改变默认处理登录成功的逻辑，只需要实现  *org.springframework.security.web.authentication.AuthenticationSuccessHandler* 接口的 *onAuthenticationSuccess* 方法即可

```java
@Component
public class MyAuthenticationSucessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(mapper.writeValueAsString(authentication));
    }
}
```

其中 *Authentication* 包含认证成功的一些信息（ip,session），也包含了用户信息，即前面提到的User对象

 要使这个配置生效，我们还的在`BrowserSecurityConfig`的`configure`中配置它

```java
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyAuthenticationSucessHandler authenticationSucessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 表单登录
                // http.httpBasic() // HTTP Basic
                .loginPage("/authentication/require") // 登录跳转 URL
                .loginProcessingUrl("/login") // 处理表单登录 URL
                .successHandler(authenticationSucessHandler) // 处理登录成功
                .and()
                .authorizeRequests() // 授权配置
                .antMatchers("/authentication/require", "/login.html").permitAll() // 登录跳转 URL 无需认证
                .anyRequest()  // 所有请求
                .authenticated() // 都需要认证
                .and().csrf().disable();
    }
}
```

