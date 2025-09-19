package com.bits.bytes.bits.bytes.Services.Impl;
import com.bits.bytes.bits.bytes.DTOs.*;
import com.bits.bytes.bits.bytes.Factories;
import com.bits.bytes.bits.bytes.MapDTOs.MapDTOs;
import com.bits.bytes.bits.bytes.Models.*;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.LeetCodeProfile;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.MyCurrentUser;
import com.bits.bytes.bits.bytes.Repo.*;
import com.bits.bytes.bits.bytes.Services.UserServices;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class UserServicesImpl implements UserServices {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProjectRepo projectRepo;

    @Autowired
    MyCurrentUser myCurrentUser;

    @Autowired
    FriendsRepo friendsRepo;

    @Autowired
    DeveloperBlogRepo developerBlogRepo;

    @Autowired
    DeveloperBlogCommentsRepo developerBlogCommentsRepo;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Factories myFactory;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserServicesImpl.class);

    @Override
    public UsersDTO findUser(String username) {
        try {
            Users user = userRepo.findByUsername(username);
            logger.info("Successfully found user: {}", username);
            return MapDTOs.mapToUsersDto(user);
        } catch (Exception e) {
            logger.error("Could not find user: {}", username, e);
            return null;
        }
    }

    @Override
    public void Register(UsersDTO usersDTO) {
        try {
            Users user = new Users();
            user.setUsername(usersDTO.getUsername());
            user.setEmail(usersDTO.getEmail());
            user.setPassword_hash(encoder.encode(usersDTO.getPassword()));
            user.setAbility_points(0);
            user.setIs_online(true);
            userRepo.save(user);
            logger.info("Successfully registered user: {}", usersDTO.getUsername());
        } catch (Exception e) {
            logger.error("Could not register user: {}", usersDTO.getUsername(), e);
        }
    }

    @Override
    public void addUserProjects(ProjectsDTO project)  {
        Users principalUser = myCurrentUser.getPrincipalUser();
        Projects newProject = myFactory.ProjectFactory();
        try {
            principalUser.addProject(project, principalUser, newProject);
            userRepo.save(principalUser);
            logger.info("Successfully added project: {} for user: {}", project.getTitle(), principalUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not add project: {} for user: {}", project.getTitle(), principalUser.getUsername(), e);
        }
    }

    @Override
    public void deleteUserProject(String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            Projects projToDelete = projectRepo.findByTitleAndUserId(title, principalUser);
            principalUser.deleteProject(projToDelete);
            userRepo.save(principalUser);
            logger.info("Successfully deleted project: {} for user: {}", title, principalUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not delete project: {} for user: {}", title, principalUser.getUsername(), e);
        }
    }

    @Override
    public void updateUserProjects(ProjectsDTO projectContents, String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            Projects projectToUpdate = projectRepo.findByTitleAndUserId(title, principalUser);
            principalUser.updateProject(projectToUpdate, principalUser, projectContents);
            userRepo.save(principalUser);
            logger.info("Successfully updated project: {} for user: {}", title, principalUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not update project: {} for user: {}", title, principalUser.getUsername(), e);
        }
    }

    /*
     * Adding a project comment requires both the project title
     * and the username of the owner.
     *
     * Reason:
     * - Multiple users can create projects with the same name.
     * - To avoid conflicts, the username is attached to the project.
     *
     * Example:
     * "Project 1" by "John Doe"
     * -> Frontend tags "John Doe" and the "Project 1" to the comment request
     * -> Backend uses this to find the correct project
     */
    @Override
    public String addCommentToProject(String title, ProjectCommentsDTO comment, String projectOwner) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            Projects projectForComment = projectRepo
                    .findByTitleAndUserId_Username(title, projectOwner)
                    .orElse(null);

            if (projectForComment == null) {
                logger.info("Could not find project: {} for owner: {}", title, projectOwner);
                return "Could not find Project Twin";
            }

            ProjectComments newComment = myFactory.ProjectCommentsFactory();
            newComment.setProjectId(projectForComment);
            newComment.setUserId(principalUser);
            newComment.setContent(comment.getContent());
            projectForComment.getComments().add(newComment);

            userRepo.save(principalUser);
            logger.info("Successfully added comment to project: {} by user: {}", title, principalUser.getUsername());
            return "Comment added Twin";
        } catch (Exception e) {
            logger.error("Could not add comment to project: {} by user: {}", title, principalUser.getUsername(), e);
            return "Failed to add comment";
        }
    }

    @Override
    public void updateUserProfile(ProfilesDTO profile) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            //Load User Profile or create a new one if null
            Profiles profileToUpdate = principalUser.getProfile() == null ? myFactory.ProfilesFactory() : principalUser.getProfile();
            profileToUpdate.setUser(principalUser);

            String url = "https://leetcode-api-faisalshohag.vercel.app/" + profile.getLeetcode_username();

            // RestTemplate used to make API calls
            // getForObject used to make a get request to my url
            // and then populates my chosen POJO
            LeetCodeProfile leetCodeData = restTemplate.getForObject(url, LeetCodeProfile.class);

            assert leetCodeData != null;
            profileToUpdate.setLeetcode_problems_solved(leetCodeData.getTotalSolved());
            profileToUpdate.setGithub_url(profile.getGithub_url());
            profileToUpdate.setProfile_photo_url(profile.getProfile_photo_url());
            profileToUpdate.setLeetcode_username(profile.getLeetcode_username());
            profileToUpdate.setLinkedin_url(profile.getLinkedin_url());
            principalUser.updateProfile(profileToUpdate);
            logger.info("Successfully updated profile for user {}", principalUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not update profile for user: {}", principalUser.getUsername(), e);
        }
        userRepo.save(principalUser);
    }

    // A part of me was thinking of naming this "dihlete" 🥀
    @Override
    public String deleteUser(String username) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        if(username.equals(principalUser.getUsername())) {
            try {
                userRepo.delete(principalUser);
                logger.info("Successfully deleted user: {}", username);
            } catch (Exception e) {
                logger.error("Could not delete user: {}", username, e);
            }
        }
        return "Profile deleted Twin on Dih!";
    }

    @Override
    public void addBugReport(BugReportsDTO bugReport) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            principalUser.addBugReport(principalUser, bugReport);
            userRepo.save(principalUser);
            logger.info("Successfully added bug report for user: {}", principalUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not add bug report for user: {}", principalUser.getUsername(), e);
        }
    }

    @Override
    public Set<String> allSentFriendRequest() {
        return myCurrentUser.getPrincipalUser().getSentFriendRequests();
    }

    @Override
    public String sendFriendRequest(FriendsDTO friendsDTO) {
        try {
            // Create a new Friend's relationship entity
            Friends newFriendRequest = new Friends();
            // Get the currently logged-in user
            Users principalUser = myCurrentUser.getPrincipalUser();
            // Find the target friend user the current user is sending the request to
            Users friendUser = userRepo.findByUsername(friendsDTO.getFriendUsername());

            // Set up the composite key parts and initial status
            newFriendRequest.setUserId(principalUser.getUserId());
            newFriendRequest.setFriendUserId(friendUser.getUserId());
            newFriendRequest.setStatus(friendsDTO.getStatus());

            // Track request for both sides:
            // add to sender’s "sent requests"
            principalUser.getSentFriendRequests().add(friendsDTO.getFriendUsername());
            // add to receiver’s "received requests"
            friendUser.getReceivedFriendRequests().add(principalUser.getUsername());

            // Save both users so their request lists update
            userRepo.save(principalUser);
            userRepo.save(friendUser);
            // Save the friend request entry
            friendsRepo.save(newFriendRequest);

            logger.info("Successfully sent friend request from {} to {}", principalUser.getUsername(), friendUser.getUsername());
            return "Friend Request sent!";
        } catch (Exception e) {
            logger.error("Could not send friend request to {}", friendsDTO.getFriendUsername(), e);
            return "Failed to send friend request";
        }
    }

    // One at a time - I pull in the username the user is accepting/declining
    @Override
    public void acceptFriendRequest(String username) {
        // Get current user
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            // Get the other user from the request AKA user_id (the sender)
            Users friendUser = userRepo.findByUsername(username);
            logger.info("Friend user {} found for acceptance", username);

            // Find the friend request entry where the current user is the receiver AKA the friend_user_id in this case
            Friends friendRequest = friendsRepo.findByFriendUserIdAndUserId(principalUser.getUserId(), friendUser.getUserId());

            // Mark the request as accepted
            friendRequest.setStatus(Friends.Status.ACCEPTED);
            // Add each user to the other's established friends list
            principalUser.getEstablishedFriends().add(friendUser.getUsername());
            friendUser.getEstablishedFriends().add(principalUser.getUsername());

            //After that, remove sent friend request from sender
            //and received friend request from receiver
            principalUser.getReceivedFriendRequests().remove(friendUser.getUsername());
            friendUser.getSentFriendRequests().remove(principalUser.getUsername());

            // Save updated users
            userRepo.save(principalUser);
            userRepo.save(friendUser);
            // Delete the original request record since it’s now resolved
            friendsRepo.save(friendRequest);

            logger.info("Successfully accepted friend request between {} and {}", principalUser.getUsername(), friendUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not accept friend request for {}", username, e);
        }
    }

    @Override
    public void declineFriendRequest(String username) {
        // Get current user
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            // Get the other user from the request AKA user_id (the sender)
            Users friendUser = userRepo.findByUsername(username);

            // Find the friend request entry where the current user is the receiver AKA the friend_user_id in this case
            Friends friendRequest = friendsRepo
                    .findByFriendUserIdAndUserId(principalUser.getUserId(), friendUser.getUserId());

            // Mark the request as declined
            friendRequest.setStatus(Friends.Status.DECLINED);

            //remove sent friend request from sender
            //and received friend request from receiver
            principalUser.getReceivedFriendRequests().remove(friendUser.getUsername());
            friendUser.getSentFriendRequests().remove(principalUser.getUsername());

            // Save updated users
            userRepo.save(principalUser);
            userRepo.save(friendUser);
            // Delete the original request record since it’s now resolved
            friendsRepo.save(friendRequest);
            logger.info("Successfully declined friend request between {} and {}", principalUser.getUsername(), friendUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not decline friend request for {}", username, e);
        }
    }

    @Override
    public void addDeveloperBlog(DeveloperBlogDTO developerBlogDTO) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            principalUser.addDeveloperBlog(developerBlogDTO, principalUser);
            logger.info("Successfully added developer blog for user: {}", principalUser.getUsername());
        } catch (Exception e) {
            logger.error("Error while adding developer blog for user: {}", principalUser.getUsername(), e);
        }
        userRepo.save(principalUser);
    }

    @Override
    public void deleteDeveloperBlog(String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            DeveloperBlog blogToDelete = developerBlogRepo.findByTitleAndUserId(title, principalUser);
            principalUser.deleteDeveloperBlog(blogToDelete);
            userRepo.save(principalUser);
            logger.info("Successfully deleted developer blog: {} for user: {}", title, principalUser.getUsername());
        } catch (Exception e) {
            logger.error("Could not delete developer blog: {} for user: {}", title, principalUser.getUsername(), e);
        }
    }

    @Override
    public String addDeveloperBlogComment(String title, String comment, String projectOwnerName) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            // Find blog directly by title + owner
            DeveloperBlog blogForComment = developerBlogRepo
                    .findByTitleAndUserId_Username(title, projectOwnerName)
                    .orElse(null);

            if (blogForComment == null) {
                logger.info("Could not find blog: {} for owner: {}", title, projectOwnerName);
                return "Could not find Blog Twin";
            }

            // Create the comment
            DeveloperBlogComments newComment = new DeveloperBlogComments();
            newComment.setBlogId(blogForComment);
            newComment.setUser(principalUser);
            newComment.setDescription(comment);
            //principalUser.getBlogComments().add(newComment);

            // Attach to blog
            blogForComment.getBlogComments().add(newComment);

            userRepo.save(principalUser);
            logger.info("Successfully added comment to blog: {} by user: {}", title, principalUser.getUsername());
            return "Comment added Twin";
        } catch (Exception e) {
            logger.error("Could not add comment to blog: {} for user: {}", title, principalUser.getUsername(), e);
            return "Failed to add comment";
        }
    }

    @Override
    public String deleteDeveloperBlogComment(String title, DeveloperBlogCommentsDTO comment, String projectOwner) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            // Find blog the comment is located in
            DeveloperBlog blogCommentIsLocatedIn = developerBlogRepo
                    .findByTitleAndUserId_Username(title, projectOwner)
                    .orElse(null);

            if (blogCommentIsLocatedIn == null) {
                logger.info("The blog '{}' does not exist for owner '{}'", title, projectOwner);
                return "Could not find blog";
            }

            List<DeveloperBlogComments> commentSet =
                    developerBlogCommentsRepo.findByDescriptionAndUser_Username(comment.getDescription(), principalUser.getUsername());

            for (DeveloperBlogComments commentToDelete : commentSet) {
                if (commentToDelete.getDescription().equals(comment.getDescription())) {
                    blogCommentIsLocatedIn.getBlogComments().remove(commentToDelete);
                    principalUser.getBlogComments().remove(commentToDelete);
                    break;
                }
            }

            developerBlogRepo.save(blogCommentIsLocatedIn);
            userRepo.save(principalUser);

            logger.info("Successfully deleted comment from blog: {} by user: {}", title, principalUser.getUsername());
            return "Comment deleted Twin";
        } catch (Exception e) {
            logger.error("Could not delete comment from blog: {} by user: {}", title, principalUser.getUsername(), e);
            return "Failed to delete comment";
        }
    }

    @Override
    public Set<DeveloperBlogDTO> getAllDevBlogs() {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            Set<DeveloperBlogDTO> blogs = MapDTOs.mapToDeveloperBlogDto(principalUser.getDeveloperBlogs());
            logger.info("Successfully loaded all developer blogs for user: {}", principalUser.getUsername());
            return blogs;
        } catch (Exception e) {
            logger.error("Could not load developer blogs for user: {}", principalUser.getUsername(), e);
            return Collections.emptySet();
        }
    }

    @Override
    public Set<BugReportsDTO> getAllBugReports() {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            Set<BugReportsDTO> bugReportsDTOSet = MapDTOs.mapToBugReportsDto(principalUser.getReports());
            logger.info("Successfully loaded all bug reports for user: {}", principalUser.getUsername());
            return bugReportsDTOSet;
        } catch (Exception e) {
            logger.error("Could not load bug reports for user: {}", principalUser.getUsername(), e);
            return Collections.emptySet();
        }
    }

}
