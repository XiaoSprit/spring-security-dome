package org.sprit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Administrator
 * @date: 2019/10/18 12:21
 */
@Controller
public class WebRequesterController {

    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello security";
    }

//    @RequestMapping("loginPage")
//    public String login(){
//        return "login";
//    }
}
