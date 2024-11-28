package com.poly.service;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.poly.entity.BookDetail;
import com.poly.entity.MailInfo;
import com.poly.serviceRepository.MailerService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailerServiceImp implements MailerService {
	List<MailInfo> listEmails = new ArrayList<>();
	@Autowired
	JavaMailSender sender;

	@Override
	public void send(MailInfo mail) throws MessagingException {
		MimeMessage message = sender.createMimeMessage();
		// Sử dụng Helper để thiết lập các thông tin cần thiết cho message
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
		helper.setFrom(mail.getFrom());
		helper.setTo(mail.getTo());
		helper.setSubject(mail.getSubject());
		helper.setText(mail.getBody(), true);
		helper.setReplyTo(mail.getFrom());

		String[] cc = mail.getCc();
		//Kiểm tra mảng cc có tồn tại hay không
		if (cc != null && cc.length > 0) {
			helper.setCc(cc);
		}
		
		String[] bcc = mail.getBcc();
		//Kiểm tra mảng bcc có tồn tại hay không
		if (bcc != null && bcc.length > 0) {
			helper.setBcc(bcc);
		}
		//Mảng file
		List<File> files = mail.getFiles();
		if (files.size()>0) {
			for (File file:files) {
				helper.addAttachment(file.getName(), file);
			}
		}
		// Gửi message đến SMTP server
		sender.send(message);
	}

	@Override
	public void send(String to, String subject, String body) throws MessagingException {
		this.send(new MailInfo(to, subject, body));
	}

	@Override
	public void queue(MailInfo mail) {
		listEmails.add(mail);
	}

	@Override
	public void queue(String to, String subject, String body) {
		queue(new MailInfo(to, subject, body));
	}

	@Scheduled(fixedDelay = 5000)
	public void run() {
		while (!listEmails.isEmpty()) {
			MailInfo mail = listEmails.remove(0);
			try {
				this.send(mail);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void sendEmail(String to, String cc, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        if (cc != null && !cc.isEmpty()) {
            message.setCc(cc.split(","));
        }
        message.setSubject(subject);
        message.setText(body);
        sender.send(message);
    }
	
}
