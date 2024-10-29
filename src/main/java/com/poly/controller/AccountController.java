package com.poly.controller;

import com.poly.auth.UserRoot;
import com.poly.dto.RegisterDto;
import com.poly.entity.Book;
import com.poly.entity.BookDetail;
import com.poly.entity.User;
import com.poly.repository.BookDetailRepository;
import com.poly.repository.UserRepo;
import com.poly.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	UserRepo userRepo;
	@Autowired
	BookDetailRepository bookDetailRepository;
	@Autowired
	private UserService userService;
	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@RequestMapping("/login")
	public String login() {
		return "account/login";
	}

//	@RequestMapping("/info")
//	public String info(Model model, Authentication auth) {
//		UserRoot userRoot = (UserRoot) auth.getPrincipal();
//		System.out.println("::::::::::::::"
//				+ userRoot.getAuthorities().stream().map(v -> v.getAuthority()).collect(Collectors.joining(", ")));
//		model.addAttribute("user", userRoot);
//		
//		return "info";
//	}
	@RequestMapping("/info")
	public String info(Model model, Authentication auth) {
	    UserRoot userRoot = (UserRoot) auth.getPrincipal();
	    
	    // Fetch the user
	    Optional<User> optionalUser = userService.findById(userRoot.getUser().getId());
	    
	    if (optionalUser.isPresent()) {
	        User user = optionalUser.get();
	        model.addAttribute("user", user);

	        // Fetch the books associated with the user
	        List<Book> books = user.getBooks(); // Assuming getBooks() is defined
	        model.addAttribute("books", books);
	        
	        // Fetch detailed information for each book
	        List<BookDetail> allBookDetails = new ArrayList<>();
	        for (Book book : books) {
	            List<BookDetail> details = bookDetailRepository.findByBook_BookCode(book.getBookCode());
	            allBookDetails.addAll(details);
	        }
	        model.addAttribute("bookDetails", allBookDetails); // Add all book details to the model

	    } else {
	        // Handle the case where user is not found
	        model.addAttribute("error", "User not found");
	    }

	    return "info";
	}
	@RequestMapping("/register")
	public String register() {
		return "account/register";
	}

	@RequestMapping("/login/success")
	public String handleLoginSuccess() {
		return "redirect:/room";
	}

	@RequestMapping("/accessDenied")
	public String accessDenied() {
		return "access/accessDenied";
	}

	@RequestMapping("/login/failure")
	public String handleLoginFailure(Model model) {
		model.addAttribute("error", "Email or password is not true");
		return "access/error";
	}

	@PostMapping("/handle-register")
	public String handleRegister(@ModelAttribute RegisterDto registerDto) {
		userRepo.save(User.builder().name(registerDto.getName())
				.password(passwordEncoder.encode(registerDto.getPassword())).email(registerDto.getEmail()).phone(registerDto.getPhone()).build());
		return "redirect:login";
	}

}
