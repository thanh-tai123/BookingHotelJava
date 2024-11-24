package com.poly.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Lazy
public class PayinfoController {
	@GetMapping("/paymentinfo")
	public String paymentInfo(@RequestParam String paymentLink, @RequestParam String qrCode, Model model) {
	    model.addAttribute("paymentUrl", paymentLink);
	    model.addAttribute("qrCodeUrl", qrCode);
	    System.out.println("QR Code URL: " + qrCode); // In ra giá trị QR code
	    return "paymentinfo"; // Tên của file HTML
	}
}
