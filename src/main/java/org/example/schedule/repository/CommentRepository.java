package org.example.schedule.repository;

import org.example.schedule.dto.Comments;
import org.example.schedule.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
    SELECT new org.example.schedule.dto.Comments(
        c.content,
        u.name,
        c.createAt,
        c.modifyAt
    )
    FROM Comment c
    JOIN c.user u
    WHERE c.schedule.id = :scheduleId
    ORDER BY c.createAt ASC
    """)
    List<Comments> findCommentsByScheduleId(@Param("scheduleId") Long scheduleId);
}
