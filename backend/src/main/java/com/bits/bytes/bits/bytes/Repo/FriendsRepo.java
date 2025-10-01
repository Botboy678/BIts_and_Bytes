package com.bits.bytes.bits.bytes.Repo;


import com.bits.bytes.bits.bytes.Models.Friends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsRepo extends JpaRepository<Friends, Integer> {
    Friends findByFriendUserIdAndUserId(Integer friendUser, Integer currentUser);

}
