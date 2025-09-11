package com.bits.bytes.bits.bytes.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
public class ProjectCommentsDTO {
    private UsersDTO userId;
    private ProjectsDTO projectId;
    private String content;
    private LocalDateTime created_at;
}
