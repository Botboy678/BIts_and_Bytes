package com.bits.bytes.bits.bytes.Services.Impl;
import com.bits.bytes.bits.bytes.DTOs.*;
import com.bits.bytes.bits.bytes.Models.*;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.MyCurrentUser;
import com.bits.bytes.bits.bytes.Repo.*;
import com.bits.bytes.bits.bytes.Services.UserServices;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserServicesImpl.class);

    @Override
    public Users findUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public void Register(UsersDTO usersDTO) {
        Users user = new Users();
        user.setUsername(usersDTO.getUsername());
        user.setEmail(usersDTO.getEmail());
        user.setPassword_hash(encoder.encode(usersDTO.getPassword()));
        user.setAbility_points(0);
        user.setIs_online(true);
        userRepo.save(user);
    }

    @Override
    public void addUserProjects(ProjectsDTO project) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        principalUser.addProject(project, principalUser);
        userRepo.save(principalUser);
    }

    @Override
    public void deleteUserProject(String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        Projects projToDelete = projectRepo.findByTitleAndUserId(title, principalUser);
        principalUser.deleteProject(projToDelete);
        userRepo.save(principalUser);
    }

    @Override
    public void updateUserProjects(ProjectsDTO projectContents, String title) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        Projects projectToUpdate = projectRepo.findByTitleAndUserId(title, principalUser);
        principalUser.updateProject(projectToUpdate, principalUser, projectContents);
        userRepo.save(principalUser);
    }

   /* I'm adding the project comment based on the title and username of
    who owns that project, this is to avoid errors bc multiple users
    could have a project with the same name
    So when I'm doing the frontend I can pull the username attached to the project
    the user wants to comment on
    Like "Project 1" by "John doe" I pull "John doe" and tag it on to send here to backend*/
    @Override
    public String addCommentToProject(String title, ProjectCommentsDTO comment, String projectOwner) {
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

        userRepo.save(principalUser);

        return "Comment added Twin";
    }

    @Override
    public void updateUserProfile(ProfilesDTO profile) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            principalUser.updateProfile(profile, principalUser);
            logger.info("Successfully updated user profile");
        } catch (Exception e) {
            logger.error("Could not update User Profile!", e);
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
                logger.info("Successfully deleted user profile");
            } catch (Exception e) {
                logger.error("Could not delete User Profile!", e);
            }
        }
        return "Profile deleted Twin on Dih!";
    }

    @Override
    public void addBugReport(BugReportsDTO bugReport) {
        Users principalUser = myCurrentUser.getPrincipalUser();
        try {
            principalUser.addBugReport(principalUser, bugReport);
            logger.info("Successfully added Bug Report!");
        } catch (Exception e) {
            logger.error("Could not add Bug Report!", e);
        }
        userRepo.save(principalUser);
    }

    @Override
    public Set<String> allSentFriendRequest() {
        return myCurrentUser.getPrincipalUser().getSentFriendRequests();
    }

    @Override
    public String sendFriendRequest(FriendsDTO friendsDTO) {
        // Create a new Friends relationship entity
        Friends newFriendRequest = new Friends();

        // Get the currently logged in user
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

        return "Friend Request sent!";
    }

    // One at a time - I pull in the username the user is accepting/declining
    @Override
    public void acceptFriendRequest(String username) {
        // Get current user
        Users principalUser = myCurrentUser.getPrincipalUser();

        // Get the other user from the request AKA user_id (the sender)
        Users friendUser = null;
        try {
            friendUser = userRepo.findByUsername(username);
            logger.info("friendUser was found!"); 
        } catch (Exception e) {
            logger.error("Could not find User by that name to add!");
        }

        // Find the friend request entry where current user is the receiver AKA the friend_user_id in this case
        assert friendUser != null;
        Friends friendRequest = friendsRepo
                .findByFriendUserIdAndUserId(principalUser.getUserId(), friendUser.getUserId());

        // Mark the request as accepted
        friendRequest.setStatus(Friends.Status.ACCEPTED);
        // Add each user to the other's established friends list
        principalUser.getEstablishedFriends().add(friendUser.getUsername());
        friendUser.getEstablishedFriends().add(principalUser.getUsername());

        //After that remove sent friend request from sender
        //and recieved friend request from reciever
        principalUser.getReceivedFriendRequests().remove(friendUser.getUsername());
        friendUser.getSentFriendRequests().remove(principalUser.getUsername());

        // Save updated users
        userRepo.save(principalUser);
        userRepo.save(friendUser);
        // Delete the original request record since it’s now resolved
        friendsRepo.save(friendRequest);
    }

    @Override
    public void declineFriendRequest(String username) {
        // Get current user
        Users principalUser = myCurrentUser.getPrincipalUser();

        // Get the other user from the request AKA user_id (the sender)
        Users friendUser = userRepo.findByUsername(username);

        // Find the friend request entry where current user is the receiver AKA the friend_user_id in this case
        Friends friendRequest = friendsRepo
                .findByFriendUserIdAndUserId(principalUser.getUserId(), friendUser.getUserId());

        // Mark the request as declined
        friendRequest.setStatus(Friends.Status.DECLINED);

        //remove sent friend request from sender
        //and recieved friend request from reciever
        principalUser.getReceivedFriendRequests().remove(friendUser.getUsername());
        friendUser.getSentFriendRequests().remove(principalUser.getUsername());

        // Save updated users
        userRepo.save(principalUser);
        userRepo.save(friendUser);
        // Delete the original request record since it’s now resolved
        friendsRepo.save(friendRequest);
    }


}
