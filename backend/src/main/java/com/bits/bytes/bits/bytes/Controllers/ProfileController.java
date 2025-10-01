package com.bits.bytes.bits.bytes.Controllers;

import com.bits.bytes.bits.bytes.DTOs.ProfilesDTO;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    UserServicesImpl userServicesImpl;
    ProfileController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody ProfilesDTO profile) {
        userServicesImpl.updateUserProfile(profile);
        logger.info("Successfully updated user profile!");
        return new ResponseEntity<>("Profile Updated Twin", HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/delete/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return new ResponseEntity<>(userServicesImpl.deleteUser(username), HttpStatus.ACCEPTED);
    }
}
