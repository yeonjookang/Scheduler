package org.example.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.request.comment.CreateCommentRequest;
import org.example.schedule.dto.request.comment.UpdateCommentRequest;
import org.example.schedule.dto.response.BaseResponse;
import org.example.schedule.dto.response.comment.CreateCommentResponse;
import org.example.schedule.dto.response.schedule.CreateScheduleResponse;
import org.example.schedule.service.CommentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules/{scheduleId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public BaseResponse<CreateCommentResponse> saveComment(@RequestBody @Validated CreateCommentRequest request,
                                                            @PathVariable Long scheduleId,
                                                            @SessionAttribute(name = "LOGIN_USER") Long userId) {
        CreateCommentResponse response = commentService.saveComment(request, scheduleId, userId);
        return BaseResponse.successOf(response);
    }

    @PutMapping("/{commentId}")
    public BaseResponse<Void> updateComment(@PathVariable Long scheduleId, @PathVariable Long commentId, @RequestBody @Validated UpdateCommentRequest request,
                                            @SessionAttribute(name = "LOGIN_USER") Long userId) {
        commentService.updateComment(scheduleId, commentId, request, userId);
        return BaseResponse.success();
    }

    @DeleteMapping("/{commentId}")
    public BaseResponse<Void> deleteComment(@PathVariable Long scheduleId, @PathVariable Long commentId,
                                            @SessionAttribute(name = "LOGIN_USER") Long userId) {
        commentService.deleteComment(scheduleId, commentId, userId);
        return BaseResponse.success();
    }
}
