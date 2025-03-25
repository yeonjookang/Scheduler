package org.example.schedule.repository;

import jakarta.validation.constraints.NotBlank;
import org.example.schedule.controller.request.dto.UpdateScheduleDto;
import org.example.schedule.controller.response.dto.GetScheduleDetailDto;
import org.example.schedule.controller.response.dto.GetSchedulesDto;
import org.example.schedule.controller.response.dto.ScheduleDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ScheduleRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void saveSchedule(Long userId, String name, String title, String content, LocalDateTime now) {
        String sql = "INSERT INTO schedule (user_id, name,title, content, create_at, modify_at) " +
                "VALUES (:userId, :name,:title, :content, :createAt, :modifyAt)";

        Map<String, Object> params = Map.of(
                "userId", userId,
                "name", name,
                "title", title,
                "content", content,
                "createAt", now,
                "modifyAt", now
        );

        jdbcTemplate.update(sql, params);
    }

    public GetSchedulesDto findSchedules(String name, String title, String date, Pageable pageable) {
        //+ 연산이 일어날 때마다 새로운 String 객체가 생성으로써 메모리 낭비 발생
        //하나의 StringBuilder 객체에 계속 append 하기 때문에 성능이 훨씬 좋음
        StringBuilder baseSql = new StringBuilder();
        //동적 쿼리를 만들기 쉽게하기 위해 where 1=1 추가
        baseSql.append("FROM schedule s WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            baseSql.append("AND s.name LIKE :name ");
            params.put("name", "%" + name + "%");
        }

        if (title != null && !title.isBlank()) {
            baseSql.append("AND s.title LIKE :title ");
            params.put("title", "%" + title + "%");
        }

        if (date != null && !date.isBlank()) {
            baseSql.append("AND DATE(s.create_at) = :date ");
            params.put("date", LocalDate.parse(date));
        }

        // 전체 카운트
        String countSql = "SELECT COUNT(*) " + baseSql;
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        // 데이터 조회
        String querySql = "SELECT s.id AS scheduleId, s.name, s.title " + baseSql +
                "ORDER BY s.create_at DESC " +
                "LIMIT :limit OFFSET :offset";

        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getPageNumber() * pageable.getPageSize());

        List<ScheduleDto> schedules = jdbcTemplate.query(
                querySql,
                params,
                (rs, rowNum) -> new ScheduleDto(
                        rs.getLong("scheduleId"),
                        rs.getString("name"),
                        rs.getString("title")
                )
        );

        int totalPages = (int) Math.ceil((double) total / pageable.getPageSize());

        return GetSchedulesDto.builder()
                .schedules(schedules)
                .totalPages(totalPages)
                .totalElements(total)
                .build();
    }

    public Optional<GetScheduleDetailDto> findScheduleDetailById(Long scheduleId) {
        String sql = """
            SELECT s.name, u.email, s.title, s.content, s.create_at, s.modify_at
            FROM schedule s
            JOIN user u ON s.user_id = u.id
            WHERE s.id = :scheduleId
        """;

        Map<String, Object> params = Map.of("scheduleId", scheduleId);

        List<GetScheduleDetailDto> results = jdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> new GetScheduleDetailDto(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getTimestamp("create_at").toLocalDateTime(),
                        rs.getTimestamp("modify_at").toLocalDateTime()
                )
        );

        return results.stream().findFirst();
    }

    public Optional<Long> findUserIdByEmailAndPassword(String email, String password) {
        String sql = "SELECT id FROM user WHERE email = :email AND password = :password";
        Map<String, Object> params = Map.of("email", email, "password", password);

        List<Long> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getLong("id"));
        return results.stream().findFirst();
    }

    public boolean isScheduleOwnedBy(Long scheduleId, Long userId) {
        String sql = "SELECT COUNT(*) FROM schedule WHERE id = :scheduleId AND user_id = :userId";
        Map<String, Object> params = Map.of("scheduleId", scheduleId, "userId", userId);
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    public void updateSchedule(Long scheduleId, UpdateScheduleDto request, LocalDateTime now) {
        StringBuilder sql = new StringBuilder("UPDATE schedule SET ");
        Map<String, Object> params = new HashMap<>();
        List<String> updates = new ArrayList<>();

        if (request.name() != null) {
            updates.add("name = :name");
            params.put("name", request.name());
        }
        if (request.title() != null) {
            updates.add("title = :title");
            params.put("title", request.title());
        }
        if (request.content() != null) {
            updates.add("content = :content");
            params.put("content", request.content());
        }

        if (updates.isEmpty()) return; // 수정할 내용이 없으면 종료

        sql.append(String.join(", ", updates));
        sql.append(", modify_at = :modifyAt WHERE id = :scheduleId");

        params.put("modifyAt", now);
        params.put("scheduleId", scheduleId);

        jdbcTemplate.update(sql.toString(), params);
    }
}
