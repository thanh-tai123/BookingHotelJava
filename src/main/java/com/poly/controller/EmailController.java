package com.poly.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.poly.entity.MailInfo;
import com.poly.helper.MailerHelper;

import com.poly.service.MailerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("mailer")
public class EmailController {
	@Autowired
	MailerService mailer;

	
//	@ResponseBody
//	@PostMapping("/send")
//	public String send(Model model, 
//	                   @RequestParam String txtTo, 
//	                   @RequestParam String txtCC,
//	                   @RequestParam String txtSubject,
//	                   @RequestParam String txtContent) {
//	    try {
//	        MailerHelper helper = new MailerHelper();
//	        String[] emailCC = helper.parseStringEmailToArray(txtCC);
//	        
//	        MailInfo mail = new MailInfo();
//	        mail.setTo(txtTo);
//	        mail.setCc(emailCC);
//	        mail.setSubject(txtSubject);
//	        mail.setBody(txtContent);
//
//	        mailer.queue(mail); // Thử gửi email
//
//	        return "<h1>Mail của bạn đã được gửi đi</h1>"; // Thành công
//	    } catch (Exception e) {
//	        e.printStackTrace(); // Ghi lại lỗi trong console
//
//	        // Trả về thông báo lỗi và chi tiết lỗi
//	        return "<h1>Không thể gửi mail: " + e.getMessage() + "</h1>";
//	    }
//	}
	 @ResponseBody
	    @PostMapping("/send")
	    public String send(
	            @RequestParam String txtTo,
	            @RequestParam String txtCC,
	            @RequestParam String txtSubject,
	            @RequestParam String txtContent,
	            HttpSession session) {
	        Integer emailCount = (Integer) session.getAttribute("emailCount");
	        if (emailCount == null) {
	            emailCount = 0;
	        }

	        if (emailCount >= 3) {
	           return getLimitReachedResponse();
	        }

	        try {
	            mailer.sendEmail(txtTo, txtCC, txtSubject, txtContent);
	            session.setAttribute("emailCount", ++emailCount);
	            return getSuccessResponse();
	        } catch (Exception e) {
	            e.printStackTrace(); // Ghi lại lỗi trong console
	            return "<h1>Không thể gửi mail: " + e.getMessage() + "</h1>";
	        }
	    }

	    private String getSuccessResponse() {
	        return "<h1>Mail của bạn đã được gửi đi</h1>" +
	                "<p>Chuyển hướng trong 4 giây...</p>" +
	                "<script>" +
	                "setTimeout(function() {" +
	                "  window.location.href = 'http://localhost:8081/room';" +
	                "}, 4000);" +
	                "</script>";
	    }
	    private String getLimitReachedResponse() {
	        return "<h1>Bạn đã gửi email tối đa 5 lần.</h1>" +
	                "<p>Chuyển hướng trong 3 giây...</p>" +
	                "<script>" +
	                "setTimeout(function() {" +
	                "  window.location.href = 'http://localhost:8081/room';" +
	                "}, 3000);" +
	                "</script>";
	    }
}
