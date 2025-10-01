package com.bits.bytes.bits.bytes.Models.MiscellaneousModels;

import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MyCurrentUser {

    @Autowired
    UserRepo userRepo;

    public Users getPrincipalUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // or throw an exception depending on your logic
        }

        String username = authentication.getName();
        return userRepo.findByUsername(username);
    }
}
