package com.bits.bytes.bits.bytes.Controllers;
import com.bits.bytes.bits.bytes.DTOs.BugReportsDTO;
import com.bits.bytes.bits.bytes.DTOs.ProfilesDTO;
import com.bits.bytes.bits.bytes.DTOs.ProjectsDTO;
import com.bits.bytes.bits.bytes.DTOs.UsersDTO;
import com.bits.bytes.bits.bytes.Models.*;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    UserServicesImpl userServicesImpl;
    UserController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfilesDTO> userProfile(Principal principal) {
        UsersDTO user = userServicesImpl.findUser(principal.getName());
        logger.info("Successfully returned user profile");
        return new ResponseEntity<>(user.getProfile(), HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<Set<ProjectsDTO>> userProjects(Principal principal) {
        UsersDTO user = userServicesImpl.findUser(principal.getName());
        return new ResponseEntity<>(user.getProject(), HttpStatus.OK);
    }

}
