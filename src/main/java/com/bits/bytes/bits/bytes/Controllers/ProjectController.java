package com.bits.bytes.bits.bytes.Controllers;

import com.bits.bytes.bits.bytes.DTOs.ProjectCommentsDTO;
import com.bits.bytes.bits.bytes.DTOs.ProjectsDTO;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    UserServicesImpl userServicesImpl;
    ProjectController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @PutMapping("/add")
    public ResponseEntity<String> addProject(@RequestBody ProjectsDTO project) {
        userServicesImpl.addUserProjects(project);
        logger.info("Project was successfully added!");
        return new ResponseEntity<>("Project added Twin", HttpStatus.ACCEPTED);
    }

    @PostMapping("/update/{title}")
    public ResponseEntity<String> UpdateProjects(@RequestBody ProjectsDTO project, @PathVariable String title) {
        userServicesImpl.updateUserProjects(project, title);
        logger.info("Project was successfully updated");
        return new ResponseEntity<>("Project Updated Twin", HttpStatus.OK);
    }

    @PutMapping("/comment/{title}/{projectOwner}")
    public ResponseEntity<String> addProjectComment(@RequestBody ProjectCommentsDTO comments, @PathVariable String projectOwner, @PathVariable String title) {
        return new ResponseEntity<>(userServicesImpl.addCommentToProject(title,comments, projectOwner), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{title}")
    public String DeleteProject(@PathVariable String title) {
        userServicesImpl.deleteUserProject(title);
        return "Project Deleted Twin!";
    }
}
