package org.sprit.security.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.sprit.controller.ValidateController;
import org.sprit.security.entity.ImageCode;
import org.sprit.security.exception.ValidateCodeException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 * 在校验密码之前先校验验证码的正确性
 * @see org.springframework.web.filter.OncePerRequestFilter，该过滤器只会执行一次
 * @author: Administrator
 * @date: 2019/10/19 14:50
 */
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    private AuthenticationFailureHandler authenticationFailureHandler;
    private SessionStrategy sessionStrategy;

    public ValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler, SessionStrategy sessionStrategy) {
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.sessionStrategy = sessionStrategy;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(StringUtils.equals("/login",request.getRequestURI())
            && StringUtils.equalsIgnoreCase("post",request.getMethod())){
            try {
                validateCode(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request,response,e);
                return;
            }
        }
        filterChain.doFilter(request,response);
    }

    /**
     * 校验输入的验证码和服务器下发的验证码（session 中）
     * @param request request
     */
    private void validateCode(ServletWebRequest request)throws ServletRequestBindingException {
        ImageCode codeInSession = (ImageCode) sessionStrategy.getAttribute(request, ValidateController.SESSION_KEY_IMAGE_CODE);
        String codeInRequester = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");

        if(StringUtils.isBlank(codeInRequester)){
            throw new ValidateCodeException("验证码不能未空");
        }

        if(codeInSession == null){
            throw new ValidateCodeException("验证码不存在");
        }

        if(codeInSession.isExpire()){
            sessionStrategy.removeAttribute(request,ValidateController.SESSION_KEY_IMAGE_CODE);
            throw new ValidateCodeException("验证码已过期");
        }

        if(!StringUtils.equalsIgnoreCase(codeInSession.getCode(),codeInRequester)){
            throw new ValidateCodeException("验证码不正确");
        }
        sessionStrategy.removeAttribute(request, ValidateController.SESSION_KEY_IMAGE_CODE);
    }
}
