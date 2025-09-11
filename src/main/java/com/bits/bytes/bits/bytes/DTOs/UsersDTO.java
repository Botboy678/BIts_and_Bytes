package com.bits.bytes.bits.bytes.DTOs;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
public class UsersDTO {
    private String username;
    private String email;
    private String password;
    private Boolean isOnline;
    private Integer abilityPoints;
    private LocalDateTime registeredAt;
    private LocalDateTime lastVisitAt;

    // Maybe a nested profile DTO instead of exposing entity directly
    private ProfilesDTO profile;
    private Set<ProjectsDTO> project;
}
