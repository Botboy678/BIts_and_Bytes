package com.bits.bytes.bits.bytes.Controllers;
import com.bits.bytes.bits.bytes.Models.Profiles;
import com.bits.bytes.bits.bytes.Models.Projects;
import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Services.UserServices;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {

    UserServices userServices;
    UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/profile")
    public Profiles userProfile(Principal principal) {
        Users user = userServices.FindUser(principal.getName());
        return user.getProfile();
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestBody Profiles profile) {
        userServices.updateUserProfile(profile);
        return "Profile Updated Twin";
    }

    @GetMapping("/projects")
    public Set<Projects> userProjects(Principal principal) {
        Users user = userServices.FindUser(principal.getName());
        return user.getProjects();
    }

    @PutMapping("/project/add")
    public String addProject(@RequestBody Projects project) {
        userServices.addUserProjects(project);
        return "Project added Twin";
    }

    @PostMapping("/project/update/{title}")
    public String UpdateProjects(@RequestBody Projects project, @PathVariable String title) {
        userServices.updateUserProjects(project, title);
        return "Project Updated Twin";
    }


    @DeleteMapping("/project/delete/{title}")
    public String DeleteProject(@PathVariable String title) {
        userServices.deleteUserProject(title);
        return "Project Deleted Twin!";
    }
}
