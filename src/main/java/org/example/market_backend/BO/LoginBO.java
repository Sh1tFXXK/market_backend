package org.example.market_backend.BO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@ToString(callSuper = true)
public class LoginBO implements Serializable {
    private static final  long serialVersionUID = 1L;
    private String token;
    private String userName;
    private String userAvatar;
    private Integer status;
    private Date LastLoginTime;
    private String userId;
    private String Mobile;
    private String address;

}
