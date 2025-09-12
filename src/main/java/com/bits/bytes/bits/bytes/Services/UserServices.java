package com.bits.bytes.bits.bytes.Services;


import com.bits.bytes.bits.bytes.DTOs.*;

import java.util.Set;

public interface UserServices {
     UsersDTO findUser(String username);
     void Register(UsersDTO usersDTO);
     void addUserProjects(ProjectsDTO project);
     void deleteUserProject(String title);
     void updateUserProjects(ProjectsDTO projectContents, String title);
     String addCommentToProject(String title, ProjectCommentsDTO comment, String projectOwner);
     void updateUserProfile(ProfilesDTO profile);
     String deleteUser(String username);
     void addBugReport(BugReportsDTO bugReport);
     Set<String> allSentFriendRequest();
     String sendFriendRequest(FriendsDTO friendsDTO);
     void acceptFriendRequest(String username);
     void declineFriendRequest(String username);
     void addDeveloperBlog(DeveloperBlogDTO developerBlogDTO);
     void deleteDeveloperBlog(String title);
     String addDeveloperBlogComment(String title, String comment, String projectOwner);
     String deleteDeveloperBlogComment(String title, DeveloperBlogCommentsDTO comment, String projectOwner);
     Set<DeveloperBlogDTO> getAllBlogs();
     Set<BugReportsDTO> getAllBugReports();
}
