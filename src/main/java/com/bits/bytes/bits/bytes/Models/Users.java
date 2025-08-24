package com.bits.bytes.bits.bytes.Models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity @Getter @Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password_hash;

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

    @OneToMany(mappedBy = "userId")
    private Set<Friends> SentFriendRequests;

    @OneToMany(mappedBy = "friendUserId")
    private Set<Friends> ReceivedFriendRequests;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "project")
    private Set<Projects> projects;

    public void addProject(Projects project, Users principalUser) {
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

    public void updateProject(Projects updatedProject, Users principalUser, Projects projectContents) {
        updatedProject.setUserId(principalUser);
        updatedProject.setTitle(projectContents.getTitle());
        updatedProject.setDescription(projectContents.getDescription());
        updatedProject.setGithub_repo_url(projectContents.getGithub_repo_url());
    }

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user_who_commented")
    private Set<ProjectComments> comments;

    @OneToMany(mappedBy = "userId")
    @JsonManagedReference(value = "bug_report")
    private Set<BugReports> reports;

    @OneToMany(mappedBy = "userId")
    private List<DeveloperBlogComments> blogComments;
}
