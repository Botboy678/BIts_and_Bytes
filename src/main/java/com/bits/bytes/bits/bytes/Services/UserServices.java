package com.bits.bytes.bits.bytes.Services;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.LeetCodeProfile;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.MyCurrentUser;
import com.bits.bytes.bits.bytes.Models.Profiles;
import com.bits.bytes.bits.bytes.Models.Projects;
import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Repo.ProfilesRepo;
import com.bits.bytes.bits.bytes.Repo.ProjectRepo;
import com.bits.bytes.bits.bytes.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Set;

@Service
public class UserServices {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProfilesRepo profilesRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    MyCurrentUser myCurrentUser;

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


}
