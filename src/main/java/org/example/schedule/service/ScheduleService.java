package org.example.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.request.schedule.CreateScheduleRequest;
import org.example.schedule.dto.request.schedule.UpdateScheduleRequest;
import org.example.schedule.dto.response.schedule.CreateScheduleResponse;
import org.example.schedule.dto.response.schedule.GetScheduleResponse;
import org.example.schedule.dto.response.schedule.GetSchedulesResponse;
import org.example.schedule.dto.ScheduleSummary;
import org.example.schedule.entity.Schedule;
import org.example.schedule.exception.ScheduleException;
import org.example.schedule.repository.ScheduleRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import static org.example.schedule.dto.response.ResponseData.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public CreateScheduleResponse saveSchedule(CreateScheduleRequest request) {
        //TODO: 세션으로 User 정보 받아와야함
        Schedule newSchedule = Schedule.builder()
                .user()
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
        return scheduleRepository.findDetailById(scheduleId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
    }

    public void updateSchedule(Long scheduleId, UpdateScheduleRequest request) {
        //TODO: 세션으로 User 정보 확인해야함
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
        findSchedule.update(request.title(), request.content());
    }

    public void deleteSchedule(Long scheduleId) {
        //TODO: 세션으로 User 정보 확인해야함
        Schedule findSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
        scheduleRepository.delete(findSchedule);
    }
}
