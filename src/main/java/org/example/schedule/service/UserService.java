package org.example.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.request.user.SignUpRequest;
import org.example.schedule.entity.User;
import org.example.schedule.exception.UserException;
import org.example.schedule.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.example.schedule.dto.response.ResponseData.USER_ALREADY_EXIST;

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
}
