package com.qushihan.work_submit_system.app.controller.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qushihan.work_submit_system.clazz.api.ClazzService;
import com.qushihan.work_submit_system.clazz.dto.ClazzDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qushihan.work_submit_system.app.util.PrintWriterUtil;
import com.qushihan.work_submit_system.inf.util.TransitionUtil;
import com.qushihan.work_submit_system.student.api.StudentService;
import com.qushihan.work_submit_system.student.dto.LoginStudentRequest;
import com.qushihan.work_submit_system.student.dto.RegisterStudentRequest;
import com.qushihan.work_submit_system.student.dto.StudentDetailRequest;
import com.qushihan.work_submit_system.student.dto.StudentDto;
import com.qushihan.work_submit_system.student.dto.StudentJoinClazzRequest;
import com.qushihan.work_submit_system.student.dto.StudentQuitClazzRequest;
import com.qushihan.work_submit_system.student.enums.JudgeLoginStatus;
import com.qushihan.work_submit_system.student.model.auto.Student;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ClazzService clazzService;

    /**
     * 学生注册
     *
     * @param registerStudentRequest
     * @param response
     */
    @PostMapping("/register")
    public void registerStudent(@RequestBody RegisterStudentRequest registerStudentRequest,
            HttpServletResponse response) {
        String registerMessge = studentService.registerStudent(registerStudentRequest);
        PrintWriterUtil.print(registerMessge, response);
    }

    /**
     * 学生登陆
     *
     * @param loginStudentRequest
     * @param request
     * @param response
     */
    @PostMapping("/login")
    public void loginStudent(@RequestBody LoginStudentRequest loginStudentRequest, HttpServletRequest request,
            HttpServletResponse response) {
        StudentDto studentDto = studentService.loginStudent(loginStudentRequest);
        String loginMessage = JudgeLoginStatus.UNKNOW.getMessage();
        if (studentDto == null) {
            loginMessage = JudgeLoginStatus.NUMBER_OR_PASSWORD_ERROR.getMessage();
        } else if (studentDto.getIsdel()) {
            loginMessage = JudgeLoginStatus.ACCOUNT_DISABLED.getMessage();
        } else {
            loginMessage = JudgeLoginStatus.LOGIN_SUCCESS.getMessage();
            List<ClazzDto> clazzDtos = clazzService.queryAllClazz();
            request.getServletContext().setAttribute("clazzDtos", clazzDtos);
            request.getServletContext().setAttribute("studentDto", studentDto);
        }
        PrintWriterUtil.print(loginMessage, response);
    }

    /**
     * 退出登陆
     *
     * @param request
     * @param response
     */
    @GetMapping("/logout")
    public void logoutStudent(HttpServletRequest request, HttpServletResponse response) {
        request.getServletContext().removeAttribute("studentDto");
    }

    /**
     * 学生加入班级
     *
     * @param studentJoinClazzRequest
     * @param response
     */
    @PostMapping("/studentJoinClazz")
    public void studentJoinClazz(@RequestBody StudentJoinClazzRequest studentJoinClazzRequest,
            HttpServletRequest request, HttpServletResponse response) {
        String joinMessage = studentService.studentJoinClazz(studentJoinClazzRequest);
        Long studentId = TransitionUtil.stringToLong(studentJoinClazzRequest.getStudentId());
        StudentDto studentDto = studentService.queryStudentByStudentId(studentId);
        request.getServletContext().setAttribute("studentDto", studentDto);
        PrintWriterUtil.print(joinMessage, response);
    }

    /**
     * 学生退出班级
     *
     * @param studentQuitClazzRequest
     * @param request
     * @param response
     */
    @RequestMapping("/studentQuitClazz")
    public void studentQuitClazz(@RequestBody StudentQuitClazzRequest studentQuitClazzRequest,
            HttpServletRequest request, HttpServletResponse response) {
        Long studentId = TransitionUtil.stringToLong(studentQuitClazzRequest.getStudentId());
        String quitMessage = studentService.studentQuitClazz(studentId);
        StudentDto studentDto = studentService.queryStudentByStudentId(studentId);
        request.getServletContext().setAttribute("studentDto", studentDto);
        PrintWriterUtil.print(quitMessage, response);
    }

    /**
     * 学生详情
     *
     * @param studentDetailRequest
     * @param request
     * @param response
     */
    @RequestMapping("/studentDetail")
    public void studentDetail(@RequestBody StudentDetailRequest studentDetailRequest, HttpServletRequest request,
            HttpServletResponse response) {
        Long clazzId = TransitionUtil.stringToLong(studentDetailRequest.getClazzId());
        List<StudentDto> studentDtos = studentService.queryStudentDtoListByClazzId(clazzId);
        request.getServletContext().setAttribute("studentDtos", studentDtos);
        PrintWriterUtil.print("学生详情查询成功", response);
    }
}
