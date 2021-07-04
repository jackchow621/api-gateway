package cn.ghost.admin.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/01 18:53
 */
@AllArgsConstructor
@Getter
public enum MatchMethodEnum {
    EQUAL((byte) 1, "="),

    REGEX((byte) 2, "regex"),

    LIKE((byte) 3, "like");


    private Byte code;

    private String desc;

    public static MatchMethodEnum getByCode(Byte code) {
        return Stream.of(values()).filter(r -> r.getCode().equals(code)).findFirst().orElse(null);
    }
}
