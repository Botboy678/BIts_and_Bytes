package com.bits.bytes.bits.bytes.MapDTOs;

import com.bits.bytes.bits.bytes.DTOs.ProfilesDTO;
import com.bits.bytes.bits.bytes.DTOs.ProjectCommentsDTO;
import com.bits.bytes.bits.bytes.DTOs.ProjectsDTO;
import com.bits.bytes.bits.bytes.DTOs.UsersDTO;
import com.bits.bytes.bits.bytes.Models.*;

import java.util.Set;
import java.util.stream.Collectors;

public class MapDTOs {

    public static ProfilesDTO mapToProfileDto(Profiles profile) {
        if (profile == null)
            return ProfilesDTO.builder()
                .github_url("No url")
                .profile_photo_url("No Photo Url")
                .linkedin_url("No LinkedinUrl")
                .leetcode_problems_solved(0)
                .leetcode_username("No username")
                .build();

        return ProfilesDTO.builder()
                .github_url(profile.getGithub_url())
                .profile_photo_url(profile.getProfile_photo_url())
                .linkedin_url(profile.getLinkedin_url())
                .leetcode_problems_solved(profile.getLeetcode_problems_solved())
                .leetcode_username(profile.getLeetcode_username())
                .build();
    }

    public static ProjectCommentsDTO mapToProjectCommentsDto(ProjectComments comment) {
        return ProjectCommentsDTO.builder()
                .userId(MapDTOs.mapToUsersDto(comment.getUserId()))
                .projectId(MapDTOs.mapToProjectDto(comment.getProjectId()))
                .content(comment.getContent())
                .build();
    }

    //Overloaded Method for the Project Comments DTO this returns a set
    public static Set<ProjectCommentsDTO> mapToProjectCommentsDto(Set<ProjectComments> comments) {
        return comments.stream()
                .map(MapDTOs::mapToProjectCommentsDto)
                .collect(Collectors.toSet());
    }

    //Overloaded method for mapping to projectDTO for returning a single instead of a set
    public static ProjectsDTO mapToProjectDto(Projects project) {
        return ProjectsDTO.builder()
                .title(project.getTitle())
                .description(project.getDescription())
                .github_repo_url(project.getGithub_repo_url())
                .comments(MapDTOs.mapToProjectCommentsDto(project.getComments()))
                .userId(project.getUserId().getUserId())
                .build();
    }

    //This could be optimized, I think...
    public static Set<ProjectsDTO> mapToProjectDto(Set<Projects> projects) {
        return projects.stream()
                .map(MapDTOs::mapToProjectDto)
                .collect(Collectors.toSet());
    }


    public static UsersDTO mapToUsersDto(Users user) {
        return UsersDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword_hash())
                .profile(MapDTOs.mapToProfileDto(user.getProfile()))
                .project(MapDTOs.mapToProjectDto(user.getProjects()))
                .build();
    }
}
