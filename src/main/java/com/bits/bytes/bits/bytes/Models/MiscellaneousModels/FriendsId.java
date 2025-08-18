package com.bits.bytes.bits.bytes.Models.MiscellaneousModels;
import lombok.Data;
import java.io.Serializable;


@Data
public class FriendsId implements Serializable {
    private Integer userId;

    private Integer friendUserId;

    public FriendsId() {}

    public FriendsId(Integer userId, Integer friendUserId) {
        this.userId = userId;
        this.friendUserId = friendUserId;
    }
}
