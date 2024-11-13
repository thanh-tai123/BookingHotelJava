package com.poly.auth;

import com.poly.entity.User;
import com.poly.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserRootService implements UserDetailsService {
  private final UserRepo userRepo;

  public UserRootService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

//  public UserDetails loadUserByUsername(String email)
//      throws UsernameNotFoundException {
//    User user = userRepo.findByEmail(email)
//        .orElseThrow(() ->
//            new UsernameNotFoundException("User not found with email : " + email)
//        );
//
//    return UserRoot.create(user);
//  }
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	    User user = userRepo.findByEmail(email).orElseThrow(() ->
	        new UsernameNotFoundException("User not found with email: " + email)
	    );

	    if (!user.getActivated()) {
	        throw new UsernameNotFoundException("User account is not activated: " + email);
	    }
	    HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("role", user.getRoleString());
       
	    return UserRoot.create(user); // Giả sử UserRoot.create trả về UserDetails
	}
}
