package com.bits.bytes.bits.bytes.Models;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity @Data @IdClass(FriendsId.class)
public class Friends {

    @Id @ManyToOne @JoinColumn(name = "user_id", nullable = false)
    private Users userId;

    @Id @ManyToOne @JoinColumn(name = "friend_user_id", nullable = false)
    private Users friendUserId;

    private enum Status {
        PENDING, APPROVED, REJECTED
    }
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @CreationTimestamp
    private LocalDateTime created_at;
}
