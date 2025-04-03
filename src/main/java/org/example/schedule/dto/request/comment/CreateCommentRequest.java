package org.example.schedule.dto.request.comment;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest (
        @NotBlank(message = "내용은 필수입니다.")
        String content
) {
}
