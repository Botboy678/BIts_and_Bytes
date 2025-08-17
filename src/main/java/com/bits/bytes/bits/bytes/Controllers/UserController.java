package com.bits.bytes.bits.bytes.Controllers;


import com.bits.bytes.bits.bytes.Models.Profiles;
import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServices userServices;

    @GetMapping("/profile")
    public Profiles userProfile(Principal principal) {
        Users user = userServices.FindUser(principal.getName());
        return user.getProfile();
    }

    @PostMapping("/profile/update")
    public String UpdateProfile(@RequestBody Profiles profile) {
        userServices.updateUserProfile(profile);
        return "Success";
    }
}
