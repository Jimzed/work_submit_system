package com.qushihan.work_submit_system.student.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 判断登陆状态枚举类
 */
@AllArgsConstructor
public enum JudgeLoginStatus {

    /**
     * 登陆成功
     */
    LOGIN_SUCCESS(0, "登陆成功"),

    /**
     * 账号或密码错误
     */
    NUMBER_OR_PASSWORD_ERROR(1, "账号或密码错误"),

    /**
     * 格式错误
     */
    FORMAT_ERROR(3, "格式错误"),
    ;

    @Getter
    private Integer code;

    @Getter
    private String message;
}
