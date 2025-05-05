package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString(callSuper = true)
public class UserInfoVO extends BaseVO {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String sign;
    private MultipartFile userAvatar;
}
