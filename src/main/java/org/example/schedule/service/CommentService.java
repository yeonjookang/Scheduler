package org.example.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.request.comment.CreateCommentRequest;
import org.example.schedule.dto.request.comment.UpdateCommentRequest;
import org.example.schedule.dto.response.comment.CreateCommentResponse;
import org.example.schedule.entity.Comment;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;
import org.example.schedule.exception.CommentException;
import org.example.schedule.exception.ScheduleException;
import org.example.schedule.exception.UserException;
import org.example.schedule.repository.CommentRepository;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.schedule.dto.response.ResponseData.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CreateCommentResponse saveComment(CreateCommentRequest request, Long scheduleId, Long userId) {
        Schedule findSchedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(request.content())
                .schedule(findSchedule)
                .user(findUser)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return new CreateCommentResponse(savedComment.getId());
    }

    public void updateComment(Long scheduleId, Long commentId, UpdateCommentRequest request, Long userId) {
        Comment findComment =  checkValidateAndReturnComment(scheduleId,commentId,userId);
        findComment.update(request.content());
    }

    public void deleteComment(Long scheduleId, Long commentId, Long userId) {
        Comment findComment =  checkValidateAndReturnComment(scheduleId,commentId,userId);
        commentRepository.delete(findComment);
    }

    private Comment checkValidateAndReturnComment(Long scheduleId, Long commentId, Long userId) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() -> new CommentException(COMMENT_NOT_FOUND));

        if (!findComment.getSchedule().getId().equals(scheduleId)) {
            throw new CommentException(COMMENT_SCHEDULE_NOT_MATCH);
        }

        if (!findComment.getUser().getId().equals(userId)) {
            throw new CommentException(USER_NOT_ALLOWED);
        }

        return findComment;
    }
}
