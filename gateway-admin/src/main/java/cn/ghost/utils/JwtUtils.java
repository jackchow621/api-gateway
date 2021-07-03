package cn.ghost.utils;

import cn.ghost.model.LocalUser;
import com.google.common.base.Charsets;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 16:49
 */
public class JwtUtils {
    /**
     * 加密算法
     */
    public static final String HEADER_ALG = "HS256";

    public static final String HEADER_TYP = "JWT";
    /**
     * 加密串
     */
    public static final String SECRET = "d5ec0a02";

    public static String generateToken(LocalUser localUser) throws Exception {
        Gson gson = new Gson();
        Header header = new Header(HEADER_ALG, HEADER_TYP);
        String encodedHeader = Base64Utils.encodeToUrlSafeString(gson.toJson(header).getBytes(
                Charsets.UTF_8));
        String encodePayLoad = Base64Utils.encodeToUrlSafeString(gson.toJson(localUser).getBytes(
                Charsets.UTF_8));
        String signature = HMACSHA256(encodedHeader + "." + encodePayLoad, SECRET);
        return encodedHeader + "." + encodePayLoad + "." + signature;
    }

    public static boolean checkSignature(String token) {
        String[] array = token.split("\\.");
        if (array.length != 3) {
            throw new IllegalArgumentException("token error");
        }
        try {
            String signature = HMACSHA256(array[0] + "." + array[1], SECRET);
            return signature.equals(array[2]);
        } catch (Exception e) {
        }
        return false;
    }

    public static LocalUser getPayLoad(String token) {
        String[] array = token.split("\\.");
        if (array.length != 3) {
            throw new IllegalArgumentException("token error");
        }
        String payLoad = new String(Base64Utils.decodeFromUrlSafeString(array[1]), Charsets.UTF_8);
        Gson gson = new Gson();
        return gson.fromJson(payLoad, LocalUser.class);
    }


    public static String HMACSHA256(String data, String key) throws Exception {

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");

        sha256_HMAC.init(secret_key);

        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();

        for (byte item : array) {

            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);

        }

        return sb.toString().toUpperCase();

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Header {

        private String alg;
        private String type;

    }

}
