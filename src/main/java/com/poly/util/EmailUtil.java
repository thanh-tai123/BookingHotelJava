package com.poly.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {

  @Autowired
  private JavaMailSender javaMailSender;

  public void sendOtpEmail(String email, String otp) throws MessagingException {
	  MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
	    mimeMessageHelper.setTo(email);
	    mimeMessageHelper.setSubject("Verify OTP");
	    String emailBody = "<div>Your OTP is: OTP_VALUE</div>" +
	                       "<div><a href='http://polyhotelbooking.online/account/verify-account?email=EMAIL_VALUE&otp=OTP_VALUE'>Click link to verify</a></div>";
	    emailBody = emailBody.replace("OTP_VALUE", otp);
	    emailBody = emailBody.replace("EMAIL_VALUE", email);
	    mimeMessageHelper.setText(emailBody, true);

	    javaMailSender.send(mimeMessage);
  }
  public void sendSetPasswordEmail(String email) throws MessagingException {
	  MimeMessage mimeMessage = javaMailSender.createMimeMessage();
	    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
	    mimeMessageHelper.setTo(email);
	    mimeMessageHelper.setSubject("Verify OTP");
	    String emailBody = 
	                       "<div><a href='http://polyhotelbooking.online/account/set-password?email=EMAIL_VALUE' target='_blank'>Click link to set password</a></div>";

	    emailBody = emailBody.replace("EMAIL_VALUE", email);
	    mimeMessageHelper.setText(emailBody, true);

	    javaMailSender.send(mimeMessage);
  }
}
