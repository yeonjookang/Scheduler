package org.example.schedule.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.request.user.LoginRequest;
import org.example.schedule.dto.request.user.SignUpRequest;
import org.example.schedule.dto.response.BaseResponse;
import org.example.schedule.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public BaseResponse<Void> signUp(@RequestBody @Validated SignUpRequest request){
        userService.signUp(request);
        return BaseResponse.success();
    }

    @PostMapping("/login")
    public BaseResponse<Void> signIn(@RequestBody @Validated LoginRequest requestDto,
                                     HttpServletRequest servletRequest, HttpServletResponse servletResponse){
        userService.login(requestDto,servletRequest, servletResponse);
        return BaseResponse.success();
    }

}
