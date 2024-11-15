package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("update")
public class LayoutController {
@GetMapping("/nav")
public String layout() {
	return "access/home";
}
}
