package org.example.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.example.schedule.dto.request.schedule.UpdateScheduleRequest;
import org.example.schedule.dto.response.BaseResponse;
import org.example.schedule.dto.request.schedule.CreateScheduleRequest;
import org.example.schedule.dto.response.schedule.CreateScheduleResponse;
import org.example.schedule.dto.response.schedule.GetScheduleResponse;
import org.example.schedule.dto.response.schedule.GetSchedulesResponse;
import org.example.schedule.service.ScheduleService;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public BaseResponse<CreateScheduleResponse> saveSchedule(@RequestBody @Validated CreateScheduleRequest request){
        CreateScheduleResponse response = scheduleService.saveSchedule(request);
        return BaseResponse.successOf(response);
    }

    @GetMapping
    public BaseResponse<GetSchedulesResponse> getSchedules(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, // yyyy-mm-dd
            @PageableDefault(size = 10, page = 0) Pageable pageable //전체 개수, 총 페이지 수, 현재 페이지 등 메타데이터 제공에다가, 페이징+정렬 한 번에 처리 가능
    ) {
        GetSchedulesResponse response = scheduleService.findSchedules(name, title, date, pageable);
        return BaseResponse.successOf(response);
    }

    @GetMapping("/{scheduleId}")
    public BaseResponse<GetScheduleResponse> getSchedule(@PathVariable Long scheduleId) {
        GetScheduleResponse schedule = scheduleService.getScheduleDetail(scheduleId);
        return BaseResponse.successOf(schedule);
    }

    @PutMapping("/{scheduleId}")
    public BaseResponse<Void> updateSchedule(@PathVariable Long scheduleId, @RequestBody @Validated UpdateScheduleRequest request) {
        scheduleService.updateSchedule(scheduleId, request);
        return BaseResponse.success();
    }

    @DeleteMapping("/{scheduleId}")
    public BaseResponse<Void> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return BaseResponse.success();
    }
}
