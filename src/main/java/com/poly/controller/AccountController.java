package com.poly.controller;

import com.poly.auth.UserRoot;
import com.poly.dto.PasswordChangeDto;
import com.poly.dto.RegisterDto;
import com.poly.entity.Book;
import com.poly.entity.BookDetail;
import com.poly.entity.User;
import com.poly.repository.BookDetailRepository;
import com.poly.repository.UserRepo;
import com.poly.service.AwsS3Service;
import com.poly.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/account")
@Lazy
public class AccountController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    BookDetailRepository bookDetailRepository;
    @Autowired
    private UserService userService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AwsS3Service awsS3Service;

    @RequestMapping("/login")
    public String login() {
        return "account/login";
    }
    @RequestMapping("/homepage")
    public String homepage() {
        return "home/homepage";
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

//    @RequestMapping("/info")
//    public String info(Model model, Authentication auth) {
//        UserRoot userRoot = (UserRoot) auth.getPrincipal();
//
//        // Fetch the user
//        Optional<User> optionalUser = userService.findById(userRoot.getUser().getId());
//
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            model.addAttribute("user", user);
//
//            // Fetch the books associated with the user
//            List<Book> books = user.getBooks(); // Assuming getBooks() is defined
//            books.sort(Comparator.comparing(Book::getCreateDate).reversed());
//            model.addAttribute("books", books);
//            model.addAttribute("books", books);
//
//            // Fetch detailed information for each book
//            List<BookDetail> allBookDetails = new ArrayList<>();
//            for (Book book : books) {
//                List<BookDetail> details = bookDetailRepository.findByBook_BookCode(book.getBookCode());
//                allBookDetails.addAll(details);
//            }
//            model.addAttribute("bookDetails", allBookDetails); // Add all book details to the model
//
//        } else {
//            // Handle the case where user is not found
//            model.addAttribute("error", "User not found");
//        }
//
//        return "account/info";
//    }

	@GetMapping("/info")
	public String info(
			Model model,
			Authentication auth,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {

		UserRoot userRoot = (UserRoot) auth.getPrincipal();

		Optional<User> optionalUser = userService.findById(userRoot.getUser().getId());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			model.addAttribute("user", user);

			// Fetch the books associated with the user and sort by date
			List<Book> books = user.getBooks();
			books.sort(Comparator.comparing(Book::getCreateDate).reversed());

			// Phân trang
			int totalPages = (int) Math.ceil((double) books.size() / size);
			int fromIndex = page * size;
			int toIndex = Math.min(fromIndex + size, books.size());

			List<Book> paginatedBooks = books.subList(fromIndex, toIndex);
			model.addAttribute("books", paginatedBooks);
			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", totalPages);
		} else {
			model.addAttribute("error", "User not found");
		}

		return "account/info";
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
        model.addAttribute("error", "EMAIL VÀ MẬT KHẨU KHÔNG KHỚP");
        return "account/login";
    }

    //	@PostMapping("/handle-register")

//	public String handleRegister(@ModelAttribute RegisterDto registerDto) {
//		userRepo.save(User.builder().name(registerDto.getName())
//				.password(passwordEncoder.encode(registerDto.getPassword())).email(registerDto.getEmail()).phone(registerDto.getPhone()).build());
//		return "redirect:login";
//	}
    @PostMapping("/handle-register")
    public String register(@ModelAttribute RegisterDto registerDto, Model model) {
        try {
            userService.register(registerDto);
            return "redirect:/account/login"; // Redirect to login after successful registration
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "account/register"; // Return to the registration page with the error message
        }
    }

    @GetMapping("/verify-account")
    public String showVerifyAccountForm(@RequestParam String email, @RequestParam String otp, Model model) {
        String result = userService.verifyAccount(email, otp);
        model.addAttribute("email", email);
        return "account/verifyAccount";
    }

    //	 @GetMapping("/verify-account")
//	    public String showVerifyAccountForm() {
//	        return "verifyAccount";
//	    }

	    @PostMapping("/verify-account")
	    public String verifyAccount(@RequestParam String email, @RequestParam String otp, Model model) {
	      try { 
	    	  String result = userService.verifyAccount(email, otp);
	    	  model.addAttribute("error",result);
	    	  return "account/login";
	      }
	      catch (IllegalArgumentException e) {
	            model.addAttribute("error", e.getMessage());
	            return "account/verifyAccount"; // Return to the registration page with the error message
	        }
	      }
	       
	       
	    
	    @GetMapping("/regenerate-otp")
	    public String showRegenerateOtpForm() {
	        return "account/regenerateOtp";
	    }
	  
//	    @PostMapping("/regenerate-otp")
//	    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
//	        String result = userService.regenerateOtp(email);
//	        return new ResponseEntity<>(result, HttpStatus.OK);
//	    }
	    @PostMapping("/regenerate-otp")
	    public String regenerateOtp(@RequestParam String email, Model model) {
	        try{
	        	String result = userService.regenerateOtp(email);
	        	 model.addAttribute("error", result);
	        	return "account/login";
	        }catch (IllegalArgumentException e) {
	            model.addAttribute("error", e.getMessage());
	            return "account/regenerateOtp"; // Return to the registration page with the error message
	        }
	    }
	    @RequestMapping("/forgot-password")
	    public String showForgotForm() {
	        return "account/forgot-password";
	    }
	
	    @RequestMapping("/set-password")
	    public String showSetPasswordForm(@RequestParam String token,Model model) {
	    	 model.addAttribute("token", token);
	    	return "account/set-password";
	    }
	    @PostMapping("/forgot-password")
	    public String forgotPassword(@RequestParam String email, Model model) {
	        try {
	            String responseMessage = userService.forgotPassword(email);
	            model.addAttribute("error",responseMessage);
	            return "account/login";
	        } catch (IllegalArgumentException e) {
	            model.addAttribute("error", e.getMessage());
	            return "account/forgot-password"; // Return to the registration page with the error message
	        }
	    }

	    @PostMapping("/set-password")
	    public String setPassword(
	            @RequestParam String token, 
	            @RequestParam String newPassword, 
	            @RequestParam String confirmPassword, 
	            Model model) {
	        // Find the user by the reset token
	        User account = userRepo.findByResetToken(token)
	                .orElseThrow(() -> new RuntimeException("MÃ TOKEN KHÔNG HỢP LỆ"));

	        // Check if the token is expired
	        if (account.getTokenExpiration().isBefore(LocalDateTime.now())) {
	            throw new RuntimeException("MÃ TOKEN ĐÃ HẾT HẠN");
	        }

	        try {
	            // Validate the password confirmation
	            if (!newPassword.equals(confirmPassword)) {
	                throw new IllegalArgumentException("MẬT KHẨU KHÔNG KHỚP");
	            }

	            // Update the user's password
	            String result = userService.setPassword(account.getEmail(), newPassword);
	            model.addAttribute("error", result);
	            
	            // Optionally clear the token after use
	            account.setResetToken(null);
	            account.setTokenExpiration(null);
	            userRepo.save(account);

	            return "account/login"; // Redirect to login or success page
	        } catch (IllegalArgumentException e) {
	            model.addAttribute("error", e.getMessage());
	            return "account/set-password"; // Return to the form with the error message
	        }
	    }
	    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	    @GetMapping("/change-password")
	    public String showChangePasswordForm(Model model, @AuthenticationPrincipal UserDetails currentUser) {
	        String username = currentUser.getUsername();
	        String password = currentUser.getPassword();
	        model.addAttribute("passwordChangeDto", new PasswordChangeDto());
	        model.addAttribute("username", username); 
	        model.addAttribute("pass", password); 
	        // Thêm tên người dùng vào mô hình
	        return "account/changepassword";
	    }

	    @PostMapping("/change-password")
	    public String changePassword(@ModelAttribute("passwordChangeDto") PasswordChangeDto passwordChangeDto,
	    @AuthenticationPrincipal UserDetails currentUser,
	    Model model) {
	        String username = currentUser.getUsername();
	        User user = userRepo.findByEmail(currentUser.getUsername()).orElse(null);

	        if (user == null) {
	            model.addAttribute("error", "User not found");
	            return "account/changepassword";
	        }

	        if (!userService.checkIfValidOldPassword(user, passwordChangeDto.getOldPassword())) {
	            model.addAttribute("error", "Old password is incorrect");
	            return "account/changepassword";
	        }

	        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmNewPassword())) {
	            model.addAttribute("error", "New passwords do not match");
	            return "account/changepassword";
	        }

	        userService.changeUserPassword(user, passwordChangeDto.getNewPassword());
	        model.addAttribute("message", "Password changed successfully. You will be redirected to the login page shortly.");

	        // Trả về trang thông báo
	        return "account/passwordChangeSuccess"; // Tạo một trang mới 'passwordChangeSuccess.html'
	    }
	    @GetMapping("user-edit")
	    public String showEditUserProfile(Principal principal, Model model) {
	        String emailLogin = principal.getName();
	        User userLogging = this.userRepo.findByEmail(emailLogin).get();
	        model.addAttribute("user", userLogging);
	        return "account/user_profile";
	    }

	    @PostMapping("/user-update")
	    public String updateUserProfile(@RequestParam(name = "image", required = false) MultipartFile image,
	                                    @RequestParam(name = "id") Long id,
	                                    @RequestParam(name = "name") String name,
	                                    @RequestParam(name = "phone") String phone) {
	      
	        User dbUser = this.userRepo.findById(id).get();

	        dbUser.setPhone(phone);
	        dbUser.setName(name);
	        if (image != null && !image.isEmpty()) {
	            String urlImageAws = this.awsS3Service.saveImageToS3(image);
	            dbUser.setImage(urlImageAws);
	        }

	        this.userRepo.save(dbUser);
	        return "redirect:/account/info";
	    }
}
