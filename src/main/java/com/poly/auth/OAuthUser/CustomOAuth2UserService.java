package com.poly.auth.OAuthUser;

import com.poly.auth.UserRoot;
import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.repository.UserRepo;
import com.poly.util._enum.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepo userRepo;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String typeOAuth = userRequest.getClientRegistration().getClientName();
        OAuth2UserInfo oAuth2UserInfo = switch (typeOAuth) {
            case "Google" -> new GoogleUserInfo(oAuth2User.getAttributes());
            default -> new GoogleUserInfo(oAuth2User.getAttributes());
        };
        
        Optional<User> userOptional = userRepo.findByEmail(oAuth2UserInfo.getEmail());
        User user;

        if (userOptional.isPresent()) {
            // Update existing user
            user = userOptional.get();
            user.generateUserCode(); // Optionally keep this if needed
            user.setName(oAuth2UserInfo.getName());
            user.setAuthType(typeOAuth.equals("Google") ? AuthTypeEnum.GOOGLE : AuthTypeEnum.FACEBOOK);
            user.setImage(oAuth2UserInfo.getImageUrl());
            user = userRepo.save(user);
        } else {
            // Create new user
            String userCode = generateUserCode(); // Generate a unique user code
            user = userRepo.save(User.builder()
                    .name(oAuth2UserInfo.getName())
                    .email(oAuth2UserInfo.getEmail())
                    .authType(AuthTypeEnum.GOOGLE) // Assuming this is Google
                    .authId(oAuth2UserInfo.getId())
                    .image(oAuth2UserInfo.getImageUrl())
                    .userCode(userCode) // Add userCode here
                    .build());
        }
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.setAttribute("userEmail", user.getEmail());
        
        String roles = user.getRoles().stream()
                .map(Role::getName) // Assuming Role class has a getName method
                .collect(Collectors.joining(","));
            session.setAttribute("userRoles", roles);
            
            session.setAttribute("userImage", user.getImage());
      
        return UserRoot.create(user, oAuth2UserInfo.getAttributes());
    }

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
   	private static final SecureRandom RANDOM = new SecureRandom();
    private String generateUserCode() {
        StringBuilder code = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            code.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return code.toString();
    }
}
