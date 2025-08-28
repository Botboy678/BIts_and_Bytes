package com.bits.bytes.bits.bytes.DTOs;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ProjectsDTO {
    private UsersDTO userId;
    private String title;
    private String description;
    private String github_repo_url;
    private LocalDateTime created_at;
    private Set<ProjectCommentsDTO> comments;
}
