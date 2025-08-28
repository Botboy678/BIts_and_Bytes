package com.bits.bytes.bits.bytes.DTOs;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
}
