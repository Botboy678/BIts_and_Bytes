package com.bits.bytes.bits.bytes.Models;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.FriendsId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity @Data @IdClass(FriendsId.class)
@Getter
@Setter
public class Friends {

    @Id @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Id @Column(name = "friend_user_id", nullable = false)
    private Integer friendUserId;

    public enum Status {
        PENDING, ACCEPTED, DECLINED
    }
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @CreationTimestamp
    private LocalDateTime created_at;
}
