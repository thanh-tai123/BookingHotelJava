package com.poly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.entity.User;
import com.poly.repository.UserRepo;

@Controller
@RequestMapping("/supplier")
@PreAuthorize("hasAuthority('SUPPLIER') or hasAuthority('ADMIN') ")
public class SupplierController {
	 @Autowired
	  private UserRepo userRepo;

	  @PreAuthorize("hasAuthority('SUPPLIER') or hasAuthority('ADMIN')")
	  @RequestMapping("")
	  public String index(Model model) {
	   
	    return "supplier";
	  }
}
