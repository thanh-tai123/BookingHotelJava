package com.poly.rescontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.UserDTO;
import com.poly.entity.User;
import com.poly.repository.UserRepo;

@RestController
@RequestMapping("/api")
public class UserRestController {

    @Autowired
    private UserRepo userRepository;

    @GetMapping("/user-info")
    public UserDTO getUserInfo(@AuthenticationPrincipal Object principal) {
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userRepository.findByEmail(userDetails.getUsername())
                .map(user -> new UserDTO(user.getId(),user.getEmail(), user.getName()))
                .orElse(null);
        } else if (principal instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) principal;
            return userRepository.findByEmail(oidcUser.getEmail())
                .map(user -> new UserDTO(user.getId(),user.getEmail(), user.getName()))
                .orElse(null);
        }
        return null;
    }

}