package cn.ghost.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnabledEnum {
    NOT_ENABLE((byte)0,"未启用"),

    ENABLE((byte)1,"启用");

    private Byte code;

    private String desc;
}
