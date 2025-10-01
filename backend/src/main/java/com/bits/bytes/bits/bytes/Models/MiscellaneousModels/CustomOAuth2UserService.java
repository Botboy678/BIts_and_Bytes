package com.bits.bytes.bits.bytes.Models.MiscellaneousModels;

import com.bits.bytes.bits.bytes.Models.Role;
import com.bits.bytes.bits.bytes.Models.Users;
import com.bits.bytes.bits.bytes.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<Users> existingUser = userRepo.findByEmail(email);

        if (existingUser.isEmpty()) {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setUsername(name != null ? name : email);
            newUser.setRole(Role.USER);
            newUser.setAbility_points(0);
            newUser.setIs_online(true);
            userRepo.save(newUser);
        }
        return oAuth2User;
    }
}
