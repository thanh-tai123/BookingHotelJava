package com.poly.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Lazy
public class DashboardController {
	 @GetMapping("dashboardadmin")
	    public String AdminDashboard(Model model) {

	        return "dashboard/index";
	    }

}
