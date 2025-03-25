package org.example.schedule.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Map;

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
}
