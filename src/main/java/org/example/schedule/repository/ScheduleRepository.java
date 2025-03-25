package org.example.schedule.repository;

import org.example.schedule.controller.response.dto.GetScheduleDetailDto;
import org.example.schedule.controller.response.dto.GetSchedulesDto;
import org.example.schedule.controller.response.dto.ScheduleDto;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ScheduleRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ScheduleRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void saveSchedule(Long userId, String title, String content, LocalDateTime now) {
        String sql = "INSERT INTO schedule (user_id, title, content, create_at, modify_at) " +
                "VALUES (:userId, :title, :content, :createAt, :modifyAt)";

        Map<String, Object> params = Map.of(
                "userId", userId,
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
        baseSql.append("FROM schedule s JOIN user u ON s.user_id = u.id WHERE 1=1 ");

        Map<String, Object> params = new HashMap<>();

        if (name != null && !name.isBlank()) {
            baseSql.append("AND u.name LIKE :name ");
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
        String querySql = "SELECT s.id AS scheduleId, u.name, u.email, s.title " + baseSql +
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
                        rs.getString("email"),
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
            SELECT u.name, u.email, s.title, s.content, s.create_at, s.modify_at
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
}
