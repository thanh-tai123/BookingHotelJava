package com.poly.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poly.dto.RegisterDto;
import com.poly.entity.User;
import com.poly.repository.UserRepo;
import com.poly.serviceRepository.UserServiceRepository;
import com.poly.util.EmailUtil;
import com.poly.util.OtpUtil;

import jakarta.mail.MessagingException;

@Service
public class UserService implements UserServiceRepository{
	 @Autowired
	  private OtpUtil otpUtil;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private EmailUtil emailUtil;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
    private AwsS3Service awsS3Service;
    public User findByName(String name) {
        return userRepository.findByName(name);
    }
   
    public String getUserEmailById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("KHÔNG TÌM THẤY NGƯỜI DÙNG"));
        return user.getEmail();
    }

	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		  return userRepository.findById(id);
	}
	  public String register(RegisterDto registerDto) {
		  if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
		        throw new IllegalArgumentException("EMAIL NÀY ĐÃ TỒN TẠI. VUI LÒNG CHỌN EMAIL KHÁC.");
		    }
		  String phone = registerDto.getPhone();
		    if (!phone.matches("0\\d{9}")) { // Số điện thoại bắt đầu bằng 0 và có tổng cộng 10 chữ số
		        throw new IllegalArgumentException("SỐ ĐIỆN THOẠI KHÔNG HỢP LỆ. VUI LÒNG NHẬP LẠI.");
		    }
		    String otp = otpUtil.generateOtp();
		    try {
		      emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
		    } catch (MessagingException e) {
		      throw new RuntimeException("KHÔNG THỂ GỬI OTP, HÃY THỬ LẠI");
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
		    return "ĐĂNG KÍ THÀNH CÔNG";
		  }
	  public String verifyAccount(String email, String otp) {
		    User Account =userRepository.findByEmail(email)
		        .orElseThrow(() -> new RuntimeException("KHÔNG TÌM THẤY TÀI KHOẢN VỚI : " + email));
		    if (Account.getOtp().equals(otp) && Duration.between(Account.getOtpGeneratedTime(),
		        LocalDateTime.now()).getSeconds() < (1 * 180)) {
		      Account.setActivated(true);
		      userRepository.save(Account);
		      return "OTP CÓ THỂ XÁC THỰC <a href=\"/account/login\">login</a>";
		    }
		    return "OTP ĐÃ HẾT HẠN, HÃY VÀO TRANG ĐĂNG NHẬP VÀ CHỌN XÁC THỰC TÀI KHOẢN ĐỂ TẠO OTP MỚI" ;
		  }
	  public String regenerateOtp(String email) {
		    User Account = userRepository.findByEmail(email)
		        .orElseThrow(() -> new RuntimeException("KHÔNG TÌM THẤY TÀI KHOẢN VỚI: " + email));
		    if (Boolean.TRUE.equals(Account.getActivated())) {
		    	 throw new IllegalArgumentException("EMAIL ĐÃ ĐƯỢC XÁC THỰC. KHÔNG CẦN GỬI OTP.");
		    }
		    String otp = otpUtil.generateOtp();
		    try {
		      emailUtil.sendOtpEmail(email, otp);
		    } catch (MessagingException e) {
		      throw new RuntimeException("KHÔNG THỂ GỬI OTP, HÃY THỬ LẠI");
		    }
		    Account.setOtp(otp);
		    Account.setOtpGeneratedTime(LocalDateTime.now());
		    userRepository.save(Account);
		    return "EMAIL ĐÃ ĐƯỢC GỬI... XIN VUI LÒNG VÀO EMAIL ĐỂ XÁC THỰC <a href=\"/account/login\">login</a>";
		  }
	  
	  
	  public String forgotPassword(String email) {
			User Account = userRepository.findByEmail(email)
					.orElseThrow(
							()-> new RuntimeException("KHÔNG TÌM THẤY TÀI KHOẢN VỚI: "+email));
					try {
						emailUtil.sendSetPasswordEmail(email);
					} catch (Exception e) {
					throw new RuntimeException("KHÔNG THỂ GỬI EMAIL");
					}
					return "VUI LÒNG KIỂM TRA EMAIL ĐỂ TẠO LẠI MẬT KHẨU" + "return <a href=\"/account/login\">login</a>";
		}
		public String setPassword(String email, String newPassword) {
			User Account =userRepository.findByEmail(email)
					.orElseThrow(
							()-> new RuntimeException("Account not found with this email: "+email));
					Account.setPassword(passwordEncoder.encode(newPassword));
					userRepository.save(Account);
					return "ĐỔI MẬT KHẨU THÀNH CÔNG" + "return <a href=\"/account/login\">login</a>";
		}

		
		 public boolean checkIfValidOldPassword(User user, String oldPassword) {
		        return passwordEncoder.matches(oldPassword, user.getPassword());
		    }

		    public void changeUserPassword(User user, String newPassword) {
		        user.setPassword(passwordEncoder.encode(newPassword));
		        userRepository.save(user);
		    }

			public List<User> getAllUsers() {
				// TODO Auto-generated method stub
				
				return userRepository.findAll();
			}

}