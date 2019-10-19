package org.sprit.security.exception;


import org.springframework.security.core.AuthenticationException;

/**
 * @author: Administrator
 * @date: 2019/10/19 15:24
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = 5022575393500654458L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
