package com.constant.enums;

/**
 * 校区的枚举类
 */
public enum Campus {
    ;

    private final Integer code;
    private final String roleName;

    Campus(Integer code, String roleName) {
        this.code = code;
        this.roleName = roleName;
    }

    public Integer getCode() {
        return code;
    }

    public String getRoleName() {
        return roleName;
    }

    public static String getCodeByName(String roleName) {
        for (Campus enums : Campus.values()) {
            if (enums.getRoleName().equals(roleName)) {
                return enums.getCode().toString();
            }
        }
        return "-1";
    }

    public static String getNameByCode(String code) {
        for (Campus enums : Campus.values()) {
            if (enums.getCode().toString().equals(code)) {
                return enums.getRoleName();
            }
        }
        return "-1";
    }
}
