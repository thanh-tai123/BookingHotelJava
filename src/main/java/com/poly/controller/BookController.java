package com.poly.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Book;
import com.poly.entity.BookDetail;
import com.poly.repository.BookDetailRepository;
import com.poly.repository.BookRepository;
import com.poly.repository.ProductRepo;
import com.poly.service.BookingService;

@Controller
@RequestMapping("/search")
public class BookController {
    @Autowired
    private BookRepository bookRepository; // Giả sử đây là repository của bạn cho Book
    @Autowired
    private BookDetailRepository bookDetailRepository; 
    @Autowired
    private BookingService bookService;
//    @GetMapping("")
//    public String searchBooking(@RequestParam(required = false) String bookCode, Model model) {
//        if (bookCode == null || bookCode.isEmpty()) {
//            model.addAttribute("error", "Book code is required.");
//            return "searchBookCode"; // Trả về template nếu không có bookCode
//        }
//        try {
//            // Tìm kiếm booking theo bookCode
//            Book booking = bookRepository.findByBookCode(bookCode);
//             
//            // Chuyển đổi Set<BookDetail> thành List<BookDetail>
//            List<BookDetail> details = bookdetailRepository.findAll();
//
//            // Kiểm tra xem bookDetailsList có dữ liệu không
//             // Ghi log dữ liệu
//            model.addAttribute("booking", booking);
//            model.addAttribute("bookDetailsList", details);
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//        }
//        return "searchBookCode"; // Trả về tên của template Thymeleaf
//    }

    @GetMapping("/bookcode")
    public String showBookForm() {
        return "searchBookCode"; // Tên của view Thymeleaf
    }

    @PostMapping("/book")
    public String getBookInfo(@RequestParam("bookCode") String bookCode, Model model) {
        Book book = bookRepository.findByBookCode(bookCode);
        List<BookDetail> details = bookDetailRepository.findByBook_BookCode(bookCode);// Tìm Book theo bookCode
        if (book != null) {
            model.addAttribute("book", book);
            model.addAttribute("bookDetails", details);
        } else {
            model.addAttribute("error", "Không tìm thấy Book với BookCode: " + bookCode);
        }
        return "searchBookCode"; // Tên của view hiển thị thông tin Book
    }
    @GetMapping("/user/books")
    public String getUserBooks(@RequestParam("Userid") Long userId, Model model) {
        List<Book> books = bookService.getBooksByUserId(userId);
        model.addAttribute("books", books);
        return "userBooks"; // Thymeleaf template name
    }
    @GetMapping("/bookdetail")
    public String showBookDetail() {
        return "bookdetail"; // Tên của view Thymeleaf
    }
}

