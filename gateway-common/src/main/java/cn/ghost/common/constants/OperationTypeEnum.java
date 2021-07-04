package cn.ghost.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @program api-gateway
 * @description:
 * @author: jackchow
 * @create: 2021/07/03 14:57
 */
@Getter
@AllArgsConstructor
public enum OperationTypeEnum {
    INSERT("INSERT","新增"),

    UPDATE("UPDATE","修改"),

    DELETE("DELETE","删除");

    private String code;

    private String desc;
}
