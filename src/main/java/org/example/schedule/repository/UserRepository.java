package org.example.schedule.repository;

import org.example.schedule.entity.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = :email";

        Map<String, Object> params = Map.of("email", email);

        List<User> results = jdbcTemplate.query(sql, params, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
            user.setModifyAt(rs.getTimestamp("modify_at").toLocalDateTime());
            return user;
        });

        return results.stream().findFirst();
    }

    public Long saveUser(String name, String email, String password, LocalDateTime now) {
        String sql = "INSERT INTO user (name, email, password, create_at, modify_at) " +
                "VALUES (:name, :email, :password, :createAt, :modifyAt)";

        MapSqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("email", email)
                .addValue("password", password)
                .addValue("createAt", now)
                .addValue("modifyAt", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, paramSource, keyHolder);

        Number key = keyHolder.getKey();
        return key != null ? key.longValue() : null;
    }
}
