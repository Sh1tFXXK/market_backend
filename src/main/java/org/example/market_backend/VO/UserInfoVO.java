package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Setter
@Getter
@ToString(callSuper = true)
public class UserInfoVO extends BaseVO implements Serializable {
    public static final  long serialVersionUID = 1L;
    /**
     * 昵称
     */
    private String userName;
    /**
     * 头像
     */
    private MultipartFile userAvatar;

}
