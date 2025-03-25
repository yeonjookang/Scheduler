package org.example.schedule.controller.request.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateScheduleDto(
        @NotBlank(message = "이메일은 필수입니다.")
        String email,
        @NotBlank(message = "비밀번호는 필수입니다.")
        String password,
        String name,
        String title,
        String content
) {}
