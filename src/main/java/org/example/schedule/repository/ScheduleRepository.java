package org.example.schedule.repository;

import org.example.schedule.dto.ScheduleSummary;
import org.example.schedule.dto.response.schedule.GetScheduleResponse;
import org.example.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;


public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("""
    SELECT new org.example.schedule.dto.ScheduleSummary(
        s.id, u.name, s.title
    )
    FROM Schedule s
    JOIN s.user u
    WHERE (:name IS NULL OR u.name LIKE %:name%)
      AND (:title IS NULL OR s.title LIKE %:title%)
      AND (:date IS NULL OR DATE(s.createAt) = :date)
    """)
    Page<ScheduleSummary> findByConditions(@Param("name") String name, @Param("title") String title, @Param("date") LocalDate date, Pageable pageable);

    @Query("""
    SELECT new org.example.schedule.dto.response.schedule.GetScheduleResponse(
        u.name,
        u.email,
        s.title,
        s.content,
        s.createAt,
        s.modifyAt
    )
    FROM Schedule s
    JOIN s.user u
    WHERE s.id = :scheduleId
    """)
    Optional<GetScheduleResponse> findDetailById(@Param("scheduleId") Long scheduleId);
}
