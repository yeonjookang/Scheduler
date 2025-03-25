package org.example.schedule.service;

import lombok.RequiredArgsConstructor;
import org.example.schedule.controller.request.dto.CreateScheduleDto;
import org.example.schedule.controller.response.dto.GetScheduleDetailDto;
import org.example.schedule.controller.response.dto.GetSchedulesDto;
import org.example.schedule.entity.User;
import org.example.schedule.exception.ScheduleException;
import org.example.schedule.exception.UserException;
import org.example.schedule.repository.ScheduleRepository;
import org.example.schedule.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.example.schedule.controller.response.ResponseData.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public void saveSchedule(CreateScheduleDto request, LocalDateTime now) {
        confirmPassword(request.password(), request.passwordConfirm());

        Long userId;
        Optional<User> user = userRepository.findByEmail(request.email());
        if(user.isPresent()) {
            checkValidUser(request.password(),user.get().getPassword());
            userId = user.get().getId();
        }
        else { userId = userRepository.saveUser(request.name(), request.email(), request.password(), now); }

        scheduleRepository.saveSchedule(userId, request.title(), request.content(), now);
    }

    private void checkValidUser(String requestPassword, String realPassword) {
        if(!requestPassword.equals(realPassword)) {
            throw new UserException(USER_NOT_FOUND);
        }
    }

    private void confirmPassword(String password, String passwordConfirm) {
        if(!password.equals(passwordConfirm)) {
            throw new ScheduleException(PASSWORD_CONFIRM_FAIL);
        }
    }

    public GetSchedulesDto findSchedules(String name, String title, String date, Pageable pageable) {
        return scheduleRepository.findSchedules(name, title, date, pageable);
    }

    public GetScheduleDetailDto getScheduleDetail(Long scheduleId) {
        return scheduleRepository.findScheduleDetailById(scheduleId)
                .orElseThrow(() -> new ScheduleException(SCHEDULE_NOT_FOUND));
    }
}
