package org.example.schedule.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest (
    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 4, message = "유저명은 4자 이내여야 합니다.")
    String name,
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    String email,
    @NotBlank(message = "비밀번호는 필수입니다.")
    String password
) {}
