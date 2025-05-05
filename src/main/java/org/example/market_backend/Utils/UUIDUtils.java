package org.example.market_backend.Utils;

public class UUIDUtils {
    public static String getUid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
