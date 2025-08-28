package com.bits.bytes.bits.bytes.Controllers;

import com.bits.bytes.bits.bytes.DTOs.ProjectCommentsDTO;
import com.bits.bytes.bits.bytes.DTOs.ProjectsDTO;
import com.bits.bytes.bits.bytes.Models.ProjectComments;
import com.bits.bytes.bits.bytes.Models.Projects;
import com.bits.bytes.bits.bytes.Services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    UserServices userServices;
    ProjectController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PutMapping("/add")
    public ResponseEntity<String> addProject(@RequestBody ProjectsDTO project) {
        userServices.addUserProjects(project);
        logger.info("Project was successfully added!");
        return new ResponseEntity<>("Project added Twin", HttpStatus.ACCEPTED);
    }

    @PostMapping("/update/{title}")
    public ResponseEntity<String> UpdateProjects(@RequestBody ProjectsDTO project, @PathVariable String title) {
        userServices.updateUserProjects(project, title);
        logger.info("Project was successfully updated");
        return new ResponseEntity<>("Project Updated Twin", HttpStatus.OK);
    }

    @PutMapping("/comment/{title}/{projectOwner}")
    public ResponseEntity<String> addProjectComment(@RequestBody ProjectCommentsDTO comments, @PathVariable String projectOwner, @PathVariable String title) {
        return new ResponseEntity<>(userServices.addCommentToProject(title,comments, projectOwner), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{title}")
    public String DeleteProject(@PathVariable String title) {
        userServices.deleteUserProject(title);
        return "Project Deleted Twin!";
    }
}
