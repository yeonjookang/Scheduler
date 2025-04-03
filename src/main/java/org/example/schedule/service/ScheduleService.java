package org.example.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.Comments;
import org.example.schedule.dto.request.schedule.CreateScheduleRequest;
import org.example.schedule.dto.request.schedule.UpdateScheduleRequest;
import org.example.schedule.dto.response.schedule.CreateScheduleResponse;
import org.example.schedule.dto.response.schedule.GetScheduleResponse;
import org.example.schedule.dto.response.schedule.GetSchedulesResponse;
import org.example.schedule.dto.ScheduleSummary;
import org.example.schedule.entity.Schedule;
import org.example.schedule.entity.User;
import org.example.schedule.exception.ScheduleException;
import org.example.schedule.exception.UserException;
import org.example.schedule.repository.CommentRepository;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static org.example.schedule.dto.response.ResponseData.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CreateScheduleResponse saveSchedule(CreateScheduleRequest request, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));

        Schedule newSchedule = Schedule.builder()
                .user(findUser)
                .title(request.title())
                .content(request.content())
                .build();
        Long scheduleId = scheduleRepository.save(newSchedule).getId();
        return new CreateScheduleResponse(scheduleId);
    }

    public GetSchedulesResponse findSchedules(String name, String title, LocalDate date, Pageable pageable) {
        Page<ScheduleSummary> page = scheduleRepository.findByConditions(name, title, date, pageable);

        return GetSchedulesResponse.builder()
                .schedules(page.getContent())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .build();
    }

    public GetScheduleResponse getScheduleDetail(Long scheduleId) {
        GetScheduleResponse response = scheduleRepository.findDetailById(scheduleId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
        List<Comments> comments = commentRepository.findCommentsByScheduleId(scheduleId);
        response.setComments(comments);
        return response;
    }

    public void updateSchedule(Long scheduleId, UpdateScheduleRequest request, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));

        if (!findUser.equals(findSchedule.getUser())) {
            throw new UserException(USER_NOT_ALLOWED);
        }

        findSchedule.update(request.title(), request.content());
    }

    public void deleteSchedule(Long scheduleId, Long userId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException(USER_NOT_FOUND));
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
        if (!findUser.equals(findSchedule.getUser())) {
            throw new UserException(USER_NOT_ALLOWED);
        }
        scheduleRepository.delete(findSchedule);
    }
}
