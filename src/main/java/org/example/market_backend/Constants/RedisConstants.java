package org.example.market_backend.Constants;

public class RedisConstants {
    public static final String REGISTER_VERIFY_CODE = "register:verify:code:%s";
    public static final long VERIFY_CODE_EXPIRE = 300L; // 验证码过期时间，单位秒
    public static final String NEW_MOBILE_VERIFY_CODE = "new:mobile:verify:code:%s";
    public static final String OLD_MOBILE_VERIFY_CODE = "old:mobile:verify:code:%s";
    public static final String TOKEN = "token:%s";
    public static final String USER_INFO = "user:info:%s";
    public static final Long TOKEN_EXPIRE = 3600L; // Token过期时间，单位秒
    public static final String USER_INFO_EXPIRE = "user:info:expire:%s";
    public static final String RETRIEVE_PWD_VERIFY_CODE = "retrieve:pwd:verify:code:%s";
    public static final String PRODUCT_ID = "product:id:%s";
    public static final String ORDER_ID = "order:id:%s";
}
