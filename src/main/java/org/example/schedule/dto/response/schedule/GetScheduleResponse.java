package org.example.schedule.dto.response.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.schedule.dto.Comments;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetScheduleResponse {
    private String name;
    private String email;
    private String title;
    private String content;
    private List<Comments> comments;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
