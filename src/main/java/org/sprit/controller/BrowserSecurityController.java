package org.sprit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 认证
 * @author: Administrator
 * @date: 2019/10/18 16:03
 */
@Controller
public class BrowserSecurityController {

    @RequestMapping("authentication/require")
    public String requireAuthentication() {
        return "login";
    }
}