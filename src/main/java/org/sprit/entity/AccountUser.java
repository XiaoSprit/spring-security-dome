package org.sprit.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Administrator
 * @date: 2019/10/18 14:59
 */
@Data
public class AccountUser implements Serializable {
    private static final long serialVersionUID = 3497935890426858541L;

    private String userName;
    private String password;
    private Boolean accountNonExpired = true;
    private Boolean accountNonLocked = true;
    private Boolean credentialsNonExpired = true;
    private Boolean enable = true;
}
