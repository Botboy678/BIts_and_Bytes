package com.bits.bytes.bits.bytes.Controllers;

import com.bits.bytes.bits.bytes.DTOs.FriendsDTO;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/friend")
public class FriendController {

    private static final Logger logger =  LoggerFactory.getLogger(FriendController.class);

    @Autowired
    UserServicesImpl userServicesImpl;

    @PutMapping("/add")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendsDTO friendsDTO) {
        return new ResponseEntity<>(userServicesImpl.sendFriendRequest(friendsDTO), HttpStatus.OK);
    }
    @PutMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody FriendsDTO friendsDTO) {
        userServicesImpl.acceptFriendRequest(friendsDTO.getFriendUsername());
        return new ResponseEntity<>("Friend Request Accepted!", HttpStatus.OK);
    }
    @PutMapping("/decline")
    public ResponseEntity<String> declineFriendRequest(@RequestBody FriendsDTO friendsDTO) {
        userServicesImpl.declineFriendRequest(friendsDTO.getFriendUsername());
        return new ResponseEntity<>("Friend Request Declined!", HttpStatus.OK);
    }

    @GetMapping("/allFriends")
    public ResponseEntity<Set<String>> allFriends() {
        return new ResponseEntity<>(userServicesImpl.allSentFriendRequest(), HttpStatus.OK);
    }
}
