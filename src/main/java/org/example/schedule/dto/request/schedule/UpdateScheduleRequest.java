package org.example.schedule.dto.request.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateScheduleRequest(
        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 10, message = "일정 제목은 10자 이내여야 합니다.")
        String title,
        @NotBlank(message = "내용은 필수입니다.")
        String content
) {}
