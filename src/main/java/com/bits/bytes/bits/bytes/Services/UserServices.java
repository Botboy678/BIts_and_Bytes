package com.bits.bytes.bits.bytes.Services;


import com.bits.bytes.bits.bytes.DTOs.*;
import com.bits.bytes.bits.bytes.Models.Users;

public interface UserServices {
     Users findUser(String username);
     void Register(UsersDTO usersDTO);
     void addUserProjects(ProjectsDTO project);
     void deleteUserProject(String title);
     void updateUserProjects(ProjectsDTO projectContents, String title);
     String addCommentToProject(String title, ProjectCommentsDTO comment, String projectOwner);
     void updateUserProfile(ProfilesDTO profile);
     String deleteUser(String username);
     void addBugReport(BugReportsDTO bugReport);
}
