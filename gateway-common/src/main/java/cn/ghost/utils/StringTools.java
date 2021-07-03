package cn.ghost.utils;

import cn.ghost.constants.MatchMethodEnum;
import cn.ghost.exception.GatewayException;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 17:31
 */
public class StringTools {
    private final static String str = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final Map<String, Pattern> PATTERN_MAP = new HashMap<>();

    /**
     * generate salt code
     * @return
     */
    public static String salt() {
        StringBuffer uuid = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            char ch = str.charAt(new Random().nextInt(str.length()));
            uuid.append(ch);
        }
        return uuid.toString();
    }

    public static String md5Digest(String value, String salt) {
        String plainText = value + salt;
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param value
     * @param matchMethod {@link MatchMethodEnum}
     * @param matchRule
     * @return
     */
    public static boolean match(String value, Byte matchMethod, String matchRule) {
        if (MatchMethodEnum.EQUAL.getCode().equals(matchMethod)) {
            return value.equals(matchRule);
        } else if (MatchMethodEnum.REGEX.getCode().equals(matchMethod)) {
            Pattern p = PATTERN_MAP.computeIfAbsent(matchRule, k -> Pattern.compile(k));
            Matcher m = p.matcher(value);
            return m.matches();
        } else if (MatchMethodEnum.LIKE.getCode().equals(matchMethod)) {
            return value.indexOf(matchRule) != -1;
        } else {
            throw new GatewayException("invalid matchMethod");
        }
    }

//    public static String byteToStr(byte[] data) {
//        try {
//            return new String(data, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
