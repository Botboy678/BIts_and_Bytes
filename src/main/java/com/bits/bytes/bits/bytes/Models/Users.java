package com.bits.bytes.bits.bytes.Models;
import com.bits.bytes.bits.bytes.DTOs.BugReportsDTO;
import com.bits.bytes.bits.bytes.DTOs.DeveloperBlogDTO;
import com.bits.bytes.bits.bytes.DTOs.ProfilesDTO;
import com.bits.bytes.bits.bytes.DTOs.ProjectsDTO;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.LeetCodeProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity @Getter @Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore
    private String password_hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @CreationTimestamp
    private LocalDateTime registered_at;

    @CreationTimestamp
    private LocalDateTime last_visit_at;

    @Column(columnDefinition = "boolean default false")
    private Boolean is_online;

    @Column(columnDefinition = "integer default 0")
    private Integer ability_points;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference(value = "user-profile")
    private Profiles profile;

    public void updateProfile(ProfilesDTO profile, Users principalUser) {
        //Load User Profile or create a new one if null
        Profiles profileToUpdate = principalUser.getProfile() == null ? new Profiles() : principalUser.getProfile();
        profileToUpdate.setUser(principalUser);

        String url = "https://leetcode-api-faisalshohag.vercel.app/" + profile.getLeetcode_username();
        // RestTemplate used to make API calls
        RestTemplate restTemplate = new RestTemplate();
        // getForObject used to make a get request to my url
        // and then populates my chosen POJO
        LeetCodeProfile leetCodeData = restTemplate.getForObject(url, LeetCodeProfile.class);

        assert leetCodeData != null;
        profileToUpdate.setLeetcode_problems_solved(leetCodeData.getTotalSolved());
        profileToUpdate.setGithub_url(profile.getGithub_url());
        profileToUpdate.setProfile_photo_url(profile.getProfile_photo_url());
        profileToUpdate.setLeetcode_username(profile.getLeetcode_username());
        profileToUpdate.setLinkedin_url(profile.getLinkedin_url());
        principalUser.setProfile(profileToUpdate);
    }

    @ElementCollection
    private Set<String> sentFriendRequests = new HashSet<>();

    @ElementCollection
    private Set<String> receivedFriendRequests = new HashSet<>();

    @ElementCollection
    private Set<String> EstablishedFriends = new HashSet<>();

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "project")
    private Set<Projects> projects;

    public void addProject(ProjectsDTO project, Users principalUser) {
        Projects newProject = new Projects();
        newProject.setUserId(principalUser);
        newProject.setTitle(project.getTitle());
        newProject.setDescription(project.getDescription());
        newProject.setGithub_repo_url(project.getGithub_repo_url());
        projects.add(newProject);
    }

    public void deleteProject(Projects project) {
        projects.remove(project);
    }

    public void updateProject(Projects updatedProject, Users principalUser, ProjectsDTO projectContents) {
        updatedProject.setUserId(principalUser);
        updatedProject.setTitle(projectContents.getTitle());
        updatedProject.setDescription(projectContents.getDescription());
        updatedProject.setGithub_repo_url(projectContents.getGithub_repo_url());
    }

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "user_who_commented")
    private Set<ProjectComments> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "bug_report")
    private Set<BugReports> reports;

    public void addBugReport(Users principalUser, BugReportsDTO bugReport) {
        BugReports newBugReport = new BugReports();
        newBugReport.setUser(principalUser);
        newBugReport.setDescription(bugReport.getDescription());
        newBugReport.setStatus(bugReport.getStatus());
        reports.add(newBugReport);
    }

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "developerBlogs")
    private Set<DeveloperBlog> developerBlogs;

    public void addDeveloperBlog(DeveloperBlogDTO developerBlogDTO, Users principalUser) {
        DeveloperBlog developerBlog = new DeveloperBlog();
        developerBlog.setAuthor(developerBlogDTO.getAuthor());
        developerBlog.setUserId(principalUser);
        developerBlog.setTitle(developerBlogDTO.getTitle());
        developerBlog.setDescription(developerBlogDTO.getDescription());
        developerBlogs.add(developerBlog);
    }

    public void deleteDeveloperBlog(DeveloperBlog developerBlog){developerBlogs.remove(developerBlog);}

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "developerBlogComment")
    private List<DeveloperBlogComments> blogComments;

}
