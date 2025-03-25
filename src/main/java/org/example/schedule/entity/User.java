package org.example.schedule.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private Long id;
    private String email;
    private String password;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}
