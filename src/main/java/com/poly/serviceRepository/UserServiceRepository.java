package com.poly.serviceRepository;

import java.util.Optional;

import com.poly.dto.RegisterDto;
import com.poly.entity.User;

public interface UserServiceRepository {
	public User findByName(String name);
	  public String getUserEmailById(Long userId);
	  public Optional<User> findById(Long id);
	  public String register(RegisterDto registerDto);
	  public String verifyAccount(String email, String otp);
	  public String regenerateOtp(String email);
	  public String forgotPassword(String email);
	  public String setPassword(String email, String newPassword) ;
	  public boolean checkIfValidOldPassword(User user, String oldPassword);
}
