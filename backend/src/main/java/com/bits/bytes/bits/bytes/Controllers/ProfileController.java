package com.bits.bytes.bits.bytes.Controllers;

import com.bits.bytes.bits.bytes.DTOs.ProfilesDTO;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    UserServicesImpl userServicesImpl;
    ProfileController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfile(
            @RequestPart("profile") ProfilesDTO profile,
            @RequestPart("file") MultipartFile file) {
        userServicesImpl.updateUserProfile(profile, file);
        logger.info("Successfully updated user profile!");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Profile Updated Twin");
    }


    @RequestMapping(value = "/delete/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        return new ResponseEntity<>(userServicesImpl.deleteUser(username), HttpStatus.ACCEPTED);
    }
}
