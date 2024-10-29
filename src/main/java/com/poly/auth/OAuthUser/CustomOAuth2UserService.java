package com.poly.auth.OAuthUser;

import com.poly.auth.UserRoot;
import com.poly.entity.Role;
import com.poly.entity.User;
import com.poly.repository.UserRepo;
import com.poly.util._enum.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
//      update
      user = userOptional.get();
      user.setName(oAuth2UserInfo.getName());
      user.setAuthType(typeOAuth.equals("Google") ? AuthTypeEnum.GOOGLE : AuthTypeEnum.FACEBOOK);
      user.setImage(oAuth2UserInfo.getImageUrl());
      user = userRepo.save(user);
    } else {
//      create
      user = userRepo.save(User.builder()
          .name(oAuth2UserInfo.getName())
          .email(oAuth2UserInfo.getEmail())
          .authType(typeOAuth.equals("Google") ? AuthTypeEnum.GOOGLE : AuthTypeEnum.FACEBOOK)
          .authId(oAuth2UserInfo.getId())
          .image(oAuth2UserInfo.getImageUrl())
          .build());
    }
    return UserRoot.create(user, oAuth2UserInfo.getAttributes());
  }
}
