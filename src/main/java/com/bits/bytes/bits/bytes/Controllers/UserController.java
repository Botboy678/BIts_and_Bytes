package com.bits.bytes.bits.bytes.Controllers;
import com.bits.bytes.bits.bytes.DTOs.BugReportsDTO;
import com.bits.bytes.bits.bytes.Models.*;
import com.bits.bytes.bits.bytes.Services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    UserServices userServices;
    UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/profile")
    public ResponseEntity<Profiles> userProfile(Principal principal) {
        Users user = userServices.findUser(principal.getName());
        logger.info("Successfully returned user profile");
        return new ResponseEntity<>(user.getProfile(), HttpStatus.OK);
    }

    @GetMapping("/projects")
    public ResponseEntity<Set<Projects>> userProjects(Principal principal) {
        return new ResponseEntity<>(userServices.findUser(principal.getName()).getProjects(), HttpStatus.OK);
    }

    @PutMapping("/bugReports/add")
    public ResponseEntity<String> addBugReport(@RequestBody BugReportsDTO bugReport) {
        userServices.addBugReport(bugReport);
        logger.info("Successfully added Bug Report");
        return new ResponseEntity<>("Bug Report Added Twin", HttpStatus.ACCEPTED);
    }

}
