package org.example.schedule.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.request.user.LoginRequest;
import org.example.schedule.dto.request.user.SignUpRequest;
import org.example.schedule.entity.User;
import org.example.schedule.exception.UserException;
import org.example.schedule.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.example.schedule.dto.response.ResponseData.USER_ALREADY_EXIST;
import static org.example.schedule.dto.response.ResponseData.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserException(USER_ALREADY_EXIST);
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .build();
         userRepository.save(user);
    }

    public void login(LoginRequest requestDto, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        User findUser = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.password(), findUser.getPassword())) {
            throw new UserException(USER_NOT_FOUND);
        }

        // 세션 없으면 생성
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute("LOGIN_USER", findUser.getId());

        // 세션 ID(UUID)를 쿠키에 담기 (기본적으로 JSESSIONID 사용됨)
        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true); //xss 공격 방지
        servletResponse.addCookie(sessionCookie);
    }
}
