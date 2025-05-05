package org.example.market_backend.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

public class TokenUtils {
    public static String getToken(String userId) {
        return DigestUtils.md5Hex(userId + new Date().getTime());
    }
    
    public static String getRequestToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            return request.getHeader("token");
        }
        return null;
    }
}
