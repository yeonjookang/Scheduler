package org.example.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.example.schedule.controller.response.BaseResponse;
import org.example.schedule.controller.request.dto.CreateScheduleDto;
import org.example.schedule.service.ScheduleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("/api/schedules")
    public BaseResponse<Void> saveSchedule(@RequestBody @Validated CreateScheduleDto request){
        scheduleService.saveSchedule(request, LocalDateTime.now());
        return BaseResponse.successOf();
    }
}
