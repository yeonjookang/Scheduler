package org.example.schedule.controller;

import lombok.RequiredArgsConstructor;
import org.example.schedule.controller.request.dto.DeleteScheduleDto;
import org.example.schedule.controller.request.dto.UpdateScheduleDto;
import org.example.schedule.controller.response.BaseResponse;
import org.example.schedule.controller.request.dto.CreateScheduleDto;
import org.example.schedule.controller.response.dto.GetScheduleDetailDto;
import org.example.schedule.controller.response.dto.GetSchedulesDto;
import org.example.schedule.service.ScheduleService;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    public BaseResponse<Void> saveSchedule(@RequestBody @Validated CreateScheduleDto request){
        scheduleService.saveSchedule(request, LocalDateTime.now());
        return BaseResponse.success();
    }

    @GetMapping
    public BaseResponse<GetSchedulesDto> getSchedules(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String date, // yyyy-MM-dd
            @PageableDefault(size = 10, page = 0) Pageable pageable //전체 개수, 총 페이지 수, 현재 페이지 등 메타데이터 제공에다가, 페이징+정렬 한 번에 처리 가능
    ) {
        GetSchedulesDto schedules = scheduleService.findSchedules(name, title, date, pageable);
        return BaseResponse.successOf(schedules);
    }

    @GetMapping("/{scheduleId}")
    public BaseResponse<GetScheduleDetailDto> getScheduleDetail(@PathVariable Long scheduleId) {
        GetScheduleDetailDto schedule = scheduleService.getScheduleDetail(scheduleId);
        return BaseResponse.successOf(schedule);
    }

    @PatchMapping("/{scheduleId}")
    public BaseResponse<Void> updateSchedule(@PathVariable Long scheduleId, @RequestBody @Validated UpdateScheduleDto request) {
        scheduleService.updateSchedule(scheduleId, request, LocalDateTime.now());
        return BaseResponse.success();
    }

    @DeleteMapping("/{scheduleId}")
    public BaseResponse<Void> deleteSchedule(@PathVariable Long scheduleId, @RequestBody @Validated DeleteScheduleDto request) {
        scheduleService.deleteSchedule(scheduleId, request);
        return BaseResponse.success();
    }
}
