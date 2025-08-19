package com.bits.bytes.bits.bytes.Services;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.LeetCodeProfile;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.MyCurrentUser;
import com.bits.bytes.bits.bytes.Models.Profiles;
import com.bits.bytes.bits.bytes.Models.ProjectComments;
import com.bits.bytes.bits.bytes.Models.Projects;
import com.bits.bytes.bits.bytes.Models.Users;
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


    public Users FindUser(String username) {
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
        Set<Projects> projects = principalUser.getProjects();
        Projects newProject = new Projects();

        //Technically this is taking in a Users object despite asking for an Id lol
        //Too lazy to go back and change so it's all good
        newProject.setUserId(principalUser);
        newProject.setTitle(project.getTitle());
        newProject.setDescription(project.getDescription());
        newProject.setGithub_repo_url(project.getGithub_repo_url());
        projects.add(newProject);

        projectRepo.save(newProject);
        userRepo.save(principalUser);
    }

    public void updateUserProjects(Projects project, String title) {

        Users principalUser = myCurrentUser.getPrincipalUser();
        Projects updatedProject = projectRepo.findByTitleAndUserId(title, principalUser);

        updatedProject.setUserId(principalUser);
        updatedProject.setTitle(project.getTitle());
        updatedProject.setDescription(project.getDescription());
        updatedProject.setGithub_repo_url(project.getGithub_repo_url());
        projectRepo.save(updatedProject);
        userRepo.save(principalUser);
    }

    public void deleteUserProject(String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        Projects projToDelete = projectRepo.findByTitleAndUserId(title, principalUser);
        Set<Projects> projects = principalUser.getProjects();
        projects.remove(projToDelete);
        projectRepo.delete(projToDelete);
        userRepo.save(principalUser);
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


    //I'm adding the project comment based on the title and username of
    //who owns that project, this is to avoid errors bc multiple users
    //could have a project with the same name
    //So when I'm doing the frontend I can pull the username attached to the project
    //the user wants to comment on
    //Like "Project 1" by "John doe" I pull "John doe" and tag it on to send here to backend
    public String addCommentToProject(String title, ProjectComments comment, String username){
        Users principalUser = myCurrentUser.getPrincipalUser();
        ProjectComments newComment = new ProjectComments();

        //Load all projects by that name
        Set<Projects> projectsSet = projectRepo.findAllByTitle(title);

        //The project the user wants to comment on
        Projects projectForComment = null;
        for(Projects project : projectsSet) {
            Users user = project.getUserId();
            if(user.getUsername().equals(username)) projectForComment = project;
        }
        if(projectForComment == null) return "Could not find Project Twin";

        //Once the project has been found
        //Get all the comments of project that is been commented on
        Set<ProjectComments> projectsCommentSet = projectForComment.getComments();
        //Loading all the comments of the principal users
        Set<ProjectComments> PrincipalUserprojectComments = principalUser.getComments();

        newComment.setProjectId(projectForComment);
        newComment.setUserId(principalUser);
        newComment.setContent(comment.getContent());
        PrincipalUserprojectComments.add(newComment);
        projectsCommentSet.add(newComment);

        projectCommentsRepo.save(newComment);
        projectRepo.save(projectForComment);
        userRepo.save(principalUser);
        return "Comment added Twin";
    }
}
