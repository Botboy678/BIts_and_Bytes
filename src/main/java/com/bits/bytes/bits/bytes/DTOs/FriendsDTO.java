package com.bits.bytes.bits.bytes.DTOs;


import com.bits.bytes.bits.bytes.Models.Friends;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendsDTO {
    private String friendUsername;
    private Friends.Status status = Friends.Status.PENDING;
    private LocalDateTime created_at;
}
