package com.bits.bytes.bits.bytes.Services;
import com.bits.bytes.bits.bytes.DTOs.*;
import com.bits.bytes.bits.bytes.Models.*;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.MyCurrentUser;
import com.bits.bytes.bits.bytes.Repo.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServices {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProfilesRepo profilesRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    MyCurrentUser myCurrentUser;

    @Autowired
    BugReportsRepo bugReportsRepo;

    @Autowired
    DeveloperBlogCommentsRepo developerBlogCommentsRepo;

    @Autowired
    ProjectCommentsRepo projectCommentsRepo;

    @Autowired
    BCryptPasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServices.class);


    public Users findUser(String username) {
        Users user = userRepo.findByUsername(username);
        return user;
    }

    public void Register(UsersDTO usersDTO) {
        Users user = new Users();
        user.setUsername(usersDTO.getUsername());
        user.setEmail(usersDTO.getEmail());
        user.setPassword_hash(encoder.encode(usersDTO.getPassword()));
        userRepo.save(user);
    }

    public void addUserProjects(ProjectsDTO project) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        principalUser.addProject(project, principalUser);
        userRepo.save(principalUser);
    }

    public void deleteUserProject(String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        Projects projToDelete = projectRepo.findByTitleAndUserId(title, principalUser);
        principalUser.deleteProject(projToDelete);
        userRepo.save(principalUser);
    }

    public void updateUserProjects(ProjectsDTO projectContents, String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        Projects projectToUpdate = projectRepo.findByTitleAndUserId(title, principalUser);
        principalUser.updateProject(projectToUpdate, principalUser, projectContents);
        userRepo.save(principalUser);
    }

    //I'm adding the project comment based on the title and username of
    //who owns that project, this is to avoid errors bc multiple users
    //could have a project with the same name
    //So when I'm doing the frontend I can pull the username attached to the project
    //the user wants to comment on
    //Like "Project 1" by "John doe" I pull "John doe" and tag it on to send here to backend
    public String addCommentToProject(String title, ProjectCommentsDTO comment, String projectOwner) {
        Users principalUser = myCurrentUser.getPrincipalUser();

        // Find project directly by title + owner
        Projects projectForComment = projectRepo
                .findByTitleAndUserId_Username(title, projectOwner)
                .orElse(null);

        if (projectForComment == null) {
            return "Could not find Project Twin";
        }

        // Create the comment
        ProjectComments newComment = new ProjectComments();
        newComment.setProjectId(projectForComment);
        newComment.setUserId(principalUser);
        newComment.setContent(comment.getContent());

        // Attach to project
        projectForComment.getComments().add(newComment);

        userRepo.save(principalUser);

        return "Comment added Twin";
    }

    public void updateUserProfile(ProfilesDTO profile) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            principalUser.updateProfile(profile, principalUser);
            logger.info("Successfully updated user profile");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Could not update User Profile!", e);
        }
        userRepo.save(principalUser);
    }

    // A part of me was thinking of naming this "dihlete" 🥀
    public String deleteUser(String username) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        if(username.equals(principalUser.getUsername())) {
            try {
                userRepo.delete(principalUser);
                logger.info("Successfully deleted user profile");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("Could not delete User Profile!", e);
            }
        }
        return "Profile deleted Twin on Dih!";
    }

    public void addBugReport(BugReportsDTO bugReport) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            principalUser.addBugReport(principalUser, bugReport);
            logger.info("Successfully added Bug Report!");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Could not add Bug Report!", e);
        }
        userRepo.save(principalUser);
    }
}
