package com.bits.bytes.bits.bytes.Services;
import com.bits.bytes.bits.bytes.Models.LeetCodeProfile;
import com.bits.bytes.bits.bytes.Models.Profiles;
import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Repo.ProfilesRepo;
import com.bits.bytes.bits.bytes.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServices {

    @Autowired
    UserRepo userRepo;

    @Autowired
    ProfilesRepo profilesRepo;

    // registering a new user
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    public void Register(Users user) {
        user.setPassword_hash(encoder.encode(user.getPassword_hash()));
        userRepo.save(user);
    }

    public Users FindUser(String username) {
        Users user = userRepo.findByUsername(username);
        return user;
    }

    public void updateUserProfile(Profiles profile) {
        // Get the current logged-in principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users principalUser = userRepo.findByUsername(username);

        Profiles newProfile = new Profiles();
        newProfile.setUser(principalUser);
        String url = "https://leetcode-api-faisalshohag.vercel.app/" + profile.getLeetcode_username();

        // RestTemplate used to make API calls
        RestTemplate restTemplate = new RestTemplate();
        // getForObject used to make a get request to my url
        // and then populates my chosen POJO
        LeetCodeProfile leetCodeData = restTemplate.getForObject(url, LeetCodeProfile.class);

        newProfile.setLeetcode_problems_solved(leetCodeData.getTotalSolved());
        newProfile.setGithub_url(profile.getGithub_url());
        newProfile.setProfile_photo_url(profile.getProfile_photo_url());
        newProfile.setLeetcode_username(profile.getLeetcode_username());
        newProfile.setLinkedin_url(profile.getLinkedin_url());
        principalUser.setProfile(newProfile);
        profilesRepo.save(newProfile);
        userRepo.save(principalUser);
    }


}
