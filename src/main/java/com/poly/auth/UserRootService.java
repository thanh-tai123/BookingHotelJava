package com.poly.auth;

import com.poly.entity.User;
import com.poly.repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserRootService implements UserDetailsService {
  private final UserRepo userRepo;

  public UserRootService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    User user = userRepo.findByEmail(email)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with email : " + email)
        );

    return UserRoot.create(user);
  }
}
