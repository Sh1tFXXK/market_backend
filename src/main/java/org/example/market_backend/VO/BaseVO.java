package org.example.market_backend.VO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

@Setter
@Getter
public class BaseVO implements java.io.Serializable{
    private String userId;
    private Long  createTime;
    private Long updateTime;
    private String token;
    private Integer  pageNum;
    private Integer  pageSize;
    private String confirmPassword;
    private String mobile;
    private String password;
    private String chatId;
    private Integer status;

    public boolean isMobile(String mobile) {
        // 简单的手机号格式验证，可以根据需要进行更复杂的正则表达式验证
        return mobile != null && mobile.matches("^1[3-9]\\d{9}$");
    }
    @Override
    public String toString() {
        return "BaseVO{" +
                "token='" + token + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }

}
