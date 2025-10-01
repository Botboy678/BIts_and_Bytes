package com.bits.bytes.bits.bytes.Models;
import com.bits.bytes.bits.bytes.DTOs.BugReportsDTO;
import com.bits.bytes.bits.bytes.DTOs.DeveloperBlogDTO;
import com.bits.bytes.bits.bytes.DTOs.ProjectsDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

    public void updateProfile(Profiles profileToUpdate) {
        this.setProfile(profileToUpdate);
    }

    @ElementCollection
    private Set<String> sentFriendRequests = new HashSet<>();

    @ElementCollection
    private Set<String> receivedFriendRequests = new HashSet<>();

    @ElementCollection
    private Set<String> EstablishedFriends = new HashSet<>();

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "project")
    private Set<Projects> projects = new HashSet<>();

    public void addProject(ProjectsDTO project, Users principalUser, Projects newProject) {
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
    private Set<BugReports> reports = new HashSet<>();

    public void addBugReport(BugReports newBugReport) {
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
