package com.bits.bytes.bits.bytes.Services;
import com.bits.bytes.bits.bytes.Models.*;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.LeetCodeProfile;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.MyCurrentUser;
import com.bits.bytes.bits.bytes.Repo.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

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


    public Users findUser(String username) {
        Users user = userRepo.findByUsername(username);
        return user;
    }

    // registering a new user
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public void Register(Users user) {
        // hash the password
        user.setPassword_hash(encoder.encode(user.getPassword_hash()));
        userRepo.save(user);
    }

    public void addUserProjects(Projects project) {
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

    public void updateUserProjects(Projects projectContents, String title) {
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
    public String addCommentToProject(String title, ProjectComments comment, String projectOwner) {
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

        projectRepo.save(projectForComment);

        return "Comment added Twin";
    }

    public void updateUserProfile(Profiles profile) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        Profiles updatedProfile = principalUser.getProfile();
        if(updatedProfile == null) {
            updatedProfile = new Profiles();
            updatedProfile.setUser(principalUser);
        }

        //This can be put into a POJO for sure do later!!!
        String url = "https://leetcode-api-faisalshohag.vercel.app/" + profile.getLeetcode_username();
        // RestTemplate used to make API calls
        RestTemplate restTemplate = new RestTemplate();
        // getForObject used to make a get request to my url
        // and then populates my chosen POJO
        LeetCodeProfile leetCodeData = restTemplate.getForObject(url, LeetCodeProfile.class);

        updatedProfile.setLeetcode_problems_solved(leetCodeData.getTotalSolved());
        updatedProfile.setGithub_url(profile.getGithub_url());
        updatedProfile.setProfile_photo_url(profile.getProfile_photo_url());
        updatedProfile.setLeetcode_username(profile.getLeetcode_username());
        updatedProfile.setLinkedin_url(profile.getLinkedin_url());

        principalUser.setProfile(updatedProfile);
        profilesRepo.save(updatedProfile);
        userRepo.save(principalUser);
    }

    public String deleteUserProfile(String username) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        if(username.equals(principalUser.getUsername())) {
            bugReportsRepo.deleteAllByUserId(principalUser);
            developerBlogCommentsRepo.deleteAllByUserId(principalUser);
            projectCommentsRepo.deleteAllByUserId(principalUser);
            projectRepo.deleteAllByUserId(principalUser);
            profilesRepo.deleteByUser(principalUser);
            userRepo.delete(principalUser);
        } else return "Wrong UserName Brah!";

        return "Profile deleted Twin on Dih!";
    }

    public void addBugReport(BugReports bugReport) {
        Users principalUser = myCurrentUser.getPrincipalUser();

        BugReports newBugReport = new BugReports();
        newBugReport.setUserId(principalUser);
        newBugReport.setDescription(bugReport.getDescription());
        newBugReport.setStatus(bugReport.getStatus());

        bugReportsRepo.save(newBugReport);
    }
}
