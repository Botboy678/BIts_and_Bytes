package com.bits.bytes.bits.bytes.DTOs;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendsDTO {
    private UsersDTO userId;
    private UsersDTO friendUserId;
    private enum Status {
        PENDING, APPROVED, REJECTED
    }

    private FriendsDTO.Status status = FriendsDTO.Status.PENDING;
    private LocalDateTime created_at;
}
