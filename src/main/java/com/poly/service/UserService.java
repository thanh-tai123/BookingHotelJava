package com.poly.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.dto.RegisterDto;
import com.poly.entity.User;
import com.poly.repository.UserRepo;
import com.poly.util.EmailUtil;
import com.poly.util.OtpUtil;

import jakarta.mail.MessagingException;

@Service
public class UserService {
	 @Autowired
	  private OtpUtil otpUtil;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private EmailUtil emailUtil;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
    public User findByName(String name) {
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
	  public String register(RegisterDto registerDto) {
		    String otp = otpUtil.generateOtp();
		    try {
		      emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
		    } catch (MessagingException e) {
		      throw new RuntimeException("Unable to send otp please try again");
		    }
		    User Account = new User();
		    Account.generateUserCode();
		    Account.setName(registerDto.getName());
		    Account.setEmail(registerDto.getEmail());
		    Account.setPhone(registerDto.getPhone());
		    Account.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		    Account.setOtp(otp);
		    Account.setOtpGeneratedTime(LocalDateTime.now());
		    userRepository.save(Account);
		    return "Account registration successful";
		  }
	  public String verifyAccount(String email, String otp) {
		    User Account =userRepository.findByEmail(email)
		        .orElseThrow(() -> new RuntimeException("Account not found with this email: " + email));
		    if (Account.getOtp().equals(otp) && Duration.between(Account.getOtpGeneratedTime(),
		        LocalDateTime.now()).getSeconds() < (1 * 180)) {
		      Account.setActivated(true);
		      userRepository.save(Account);
		      return "OTP verified you can <a href=\"/account/login\">login</a>";
		    }
		    return "Please regenerate otp and try again" ;
		  }
	  public String regenerateOtp(String email) {
		    User Account = userRepository.findByEmail(email)
		        .orElseThrow(() -> new RuntimeException("Account not found with this email: " + email));
		    String otp = otpUtil.generateOtp();
		    try {
		      emailUtil.sendOtpEmail(email, otp);
		    } catch (MessagingException e) {
		      throw new RuntimeException("Unable to send otp please try again");
		    }
		    Account.setOtp(otp);
		    Account.setOtpGeneratedTime(LocalDateTime.now());
		    userRepository.save(Account);
		    return "Email sent... please verify account within 1 minute <a href=\"/account/login\">login</a>";
		  }
	  
	  
	  public String forgotPassword(String email) {
			User Account = userRepository.findByEmail(email)
					.orElseThrow(
							()-> new RuntimeException("Account not found with this email: "+email));
					try {
						emailUtil.sendSetPasswordEmail(email);
					} catch (Exception e) {
					throw new RuntimeException("Unable to send password");
					}
					return "Please check your email to set new password" + "return <a href=\"/account/login\">login</a>";
		}
		public String setPassword(String email, String newPassword) {
			User Account =userRepository.findByEmail(email)
					.orElseThrow(
							()-> new RuntimeException("Account not found with this email: "+email));
					Account.setPassword(passwordEncoder.encode(newPassword));
					userRepository.save(Account);
					return "New Passoword is successfully" + "return <a href=\"/account/login\">login</a>";
		}
		
		 public boolean checkIfValidOldPassword(User user, String oldPassword) {
		        return passwordEncoder.matches(oldPassword, user.getPassword());
		    }

		    public void changeUserPassword(User user, String newPassword) {
		        user.setPassword(passwordEncoder.encode(newPassword));
		        userRepository.save(user);
		    }
}