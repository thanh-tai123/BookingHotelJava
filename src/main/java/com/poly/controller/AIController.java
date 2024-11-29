package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class AIController {
	@RequestMapping("/chatai")
	public String index() {
		return "pagetem/index";
	}
//	@RequestMapping("/chatai")
//	public String index() {
//		return "redirect:/page/index.html";
//	}
}
