package com.bits.bytes.bits.bytes.Controllers;


import com.bits.bytes.bits.bytes.DTOs.DeveloperBlogCommentsDTO;
import com.bits.bytes.bits.bytes.DTOs.DeveloperBlogDTO;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/DeveloperBlog")
public class DeveloperController {
    private static final Logger logger = LoggerFactory.getLogger(DeveloperController.class);

    UserServicesImpl userServicesImpl;
    DeveloperController(UserServicesImpl userServicesImpl) {
        this.userServicesImpl = userServicesImpl;
    }

    @PutMapping("/add")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<String> addBlog(@RequestBody DeveloperBlogDTO blog) {
        userServicesImpl.addDeveloperBlog(blog);
        logger.info("Blog was successfully added!");
        return new ResponseEntity<>("Blog added Twin", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{title}")
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<String> deleteBlog(@PathVariable String title) {
        userServicesImpl.deleteDeveloperBlog(title);
        logger.info("Blog was successfully deleted!");
        return new ResponseEntity<>("Blog deleted Twin", HttpStatus.ACCEPTED);
    }

    @PutMapping("/comment/{title}/{projectOwner}")
    public ResponseEntity<String> addDeveloperBlogComment(@RequestBody DeveloperBlogCommentsDTO comments, @PathVariable String projectOwner, @PathVariable String title) {
        return new ResponseEntity<>(userServicesImpl.addDeveloperBlogComment(title, comments.getDescription(), projectOwner), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/comment/{title}/{projectOwnerName}")
    public ResponseEntity<String> deleteDeveloperBlogComment(@RequestBody DeveloperBlogCommentsDTO comments, @PathVariable String projectOwnerName, @PathVariable String title) {
        return new ResponseEntity<>(userServicesImpl.deleteDeveloperBlogComment(title, comments, projectOwnerName), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getAllUsersBlogs")
    public Set<DeveloperBlogDTO> getAllUsersBlogs(){
        return userServicesImpl.getAllBlogs();
    }

}
