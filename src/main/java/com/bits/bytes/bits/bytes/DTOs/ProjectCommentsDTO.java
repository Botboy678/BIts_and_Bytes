package com.bits.bytes.bits.bytes.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ProjectCommentsDTO {
    private UsersDTO userId;
    private ProjectsDTO projectId;
    private String Content;
    private LocalDateTime created_at;
}
