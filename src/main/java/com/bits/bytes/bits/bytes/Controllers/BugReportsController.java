package com.bits.bytes.bits.bytes.Controllers;

import com.bits.bytes.bits.bytes.DTOs.BugReportsDTO;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/Bugreport")
public class BugReportsController {

    private static final Logger logger = LoggerFactory.getLogger(BugReportsController.class);

    UserServicesImpl userServicesImpl;
    BugReportsController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @PutMapping("/add")
    public ResponseEntity<String> addBugReport(@RequestBody BugReportsDTO bugReport) {
        userServicesImpl.addBugReport(bugReport);
        logger.info("User added Bug Report");
        return new ResponseEntity<>("Bug Report Added!", HttpStatus.ACCEPTED);
    }

    @GetMapping("/allBugReports")
    public Set<BugReportsDTO> getAllBugReports() {
        return userServicesImpl.getAllBugReports();
    }
}
