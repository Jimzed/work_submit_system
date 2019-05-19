package com.qushihan.work_submit_system.student.dto;

import lombok.Data;

/**
 * 搜索学生请求
 */
@Data
public class GetStudentBySearchRequest {

    /**
     * 搜索学生名字
     */
    private String searchStudentName;
}