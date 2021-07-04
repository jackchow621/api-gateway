package cn.ghost.admin.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MatchObjectEnum {

    DEFAULT("DEFAULT", "默认"),

    QUERY("QUERY", "参数"),

    HEADER("HEADER", "头部");

    private String code;

    private String desc;

}
