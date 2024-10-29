package com.poly.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.User;
import com.poly.repository.UserRepo;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    public User findByUsername(String name) {
        return userRepository.findByName(name);
    }
    
    public String getUserEmailById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getEmail();
    }

	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		  return userRepository.findById(id);
	}

}