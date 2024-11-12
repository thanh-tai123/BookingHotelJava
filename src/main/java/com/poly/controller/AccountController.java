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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
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
	        books.sort(Comparator.comparing(Book::getCreateDate).reversed());
	        model.addAttribute("books", books);
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
		return "account/login";
	}

//	@PostMapping("/handle-register")
//	public String handleRegister(@ModelAttribute RegisterDto registerDto) {
//		userRepo.save(User.builder().name(registerDto.getName())
//				.password(passwordEncoder.encode(registerDto.getPassword())).email(registerDto.getEmail()).phone(registerDto.getPhone()).build());
//		return "redirect:login";
//	}
	 @PostMapping("/handle-register")
	    public String register(@ModelAttribute RegisterDto registerDto) {
	        userService.register(registerDto);
	        return "redirect:/account/login"; // Sau khi đăng ký, chuyển hướng người dùng đến trang đăng nhập
	    }
	 @GetMapping("/verify-account")
	    public String showVerifyAccountForm(@RequestParam String email, @RequestParam String otp, Model model) {
		 String result = userService.verifyAccount(email, otp);
	        model.addAttribute("email", email);
	        return "verifyAccount";
	    }
//	 @GetMapping("/verify-account")
//	    public String showVerifyAccountForm() {
//	        return "verifyAccount";
//	    }
	    @PostMapping("/verify-account")
	    public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp) {
	        String result = userService.verifyAccount(email, otp);
	       
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    }
	    @GetMapping("/regenerate-otp")
	    public String showRegenerateOtpForm() {
	        return "regenerateOtp";
	    }

	    @PostMapping("/regenerate-otp")
	    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
	        String result = userService.regenerateOtp(email);
	        return new ResponseEntity<>(result, HttpStatus.OK);
	    }
	    @RequestMapping("/forgot-password")
	    public String showForgotForm() {
	        return "forgot-password";
	    }
	
	    @RequestMapping("/set-password")
	    public String showSetPasswordForm(@RequestParam String email,Model model) {
	    	 model.addAttribute("email", email);
	    	return "set-password";
	    }
	    @PostMapping("/forgot-password")
	    public ResponseEntity<String> forgotPassword(@RequestParam String email){
	    	return new ResponseEntity<>(userService.forgotPassword(email),HttpStatus.OK);
	    }
	    @PostMapping("/set-password")
	    public ResponseEntity<String> setPassword(@RequestParam String email, @RequestParam String newPassword){
	    	return new ResponseEntity<>(userService.setPassword(email, newPassword),HttpStatus.OK);
	    }
}
