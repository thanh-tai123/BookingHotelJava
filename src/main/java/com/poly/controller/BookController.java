package com.poly.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public String showBookForm(Model model) {
    	model.addAttribute("bookCode", "");
        return "searchBookCode"; // Tên của view Thymeleaf
    }

    @GetMapping("/getbook")
    public String getBook(@RequestParam("bookCode") String bookCode, Model model) {
        Book book = bookRepository.findByBookCode(bookCode);
        List<BookDetail> details = bookDetailRepository.findByBook_BookCode(bookCode);// Tìm Book theo bookCode
        if (book != null) {
            model.addAttribute("book", book);
            model.addAttribute("bookDetails", book.getBookDetails());
        } else {
            model.addAttribute("error", "Không tìm thấy Book với BookCode: " + bookCode);
        }
        model.addAttribute("bookCode", bookCode);
        return "searchBookCode"; // Tên của view hiển thị thông tin Book
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
        model.addAttribute("bookCode", bookCode);
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
    
    
    
	/* -----ADMIN, STAFF UPDATE BOOKDETAILSTATUS */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/bookcode")
    public String showAdminBookForm(Model model) {
    	model.addAttribute("bookCode", "");
        return "admin/searchBookCode"; // Tên của view Thymeleaf
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/getbook")
    public String getAdminBook(@RequestParam("bookCode") String bookCode, Model model) {
        Book book = bookRepository.findByBookCode(bookCode);
        List<BookDetail> details = bookDetailRepository.findByBook_BookCode(bookCode);// Tìm Book theo bookCode
        if (book != null) {
            model.addAttribute("book", book);
            model.addAttribute("bookDetails", details);
        } else {
            model.addAttribute("error", "Không tìm thấy Book với BookCode: " + bookCode);
        }
        model.addAttribute("bookCode", bookCode);
        return "/admin/searchBookCode"; // Tên của view hiển thị thông tin Book
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/book")
    public String getAdminBookInfo(@RequestParam("bookCode") String bookCode, Model model) {
        Book book = bookRepository.findByBookCode(bookCode);
        List<BookDetail> details = bookDetailRepository.findByBook_BookCode(bookCode);// Tìm Book theo bookCode
        if (book != null) {
            model.addAttribute("book", book);
            model.addAttribute("bookDetails", details);
        } else {
            model.addAttribute("error", "Không tìm thấy Book với BookCode: " + bookCode);
        }
        model.addAttribute("bookCode", bookCode);
        return "/admin/searchBookCode"; // Tên của view hiển thị thông tin Book
    }
    @PostMapping("/admin/updateStatus")
    public String updateStatus(@RequestParam("detailId") Integer detailId,
                               @RequestParam("status") String status,
                               @RequestParam("bookCode") String bookCode,
                               @AuthenticationPrincipal UserDetails currentUser, // Lấy thông tin người dùng hiện tại
                               Model model) {
        Optional<BookDetail> optionalBookDetail = bookDetailRepository.findById(detailId);
        if (optionalBookDetail.isPresent()) {
            BookDetail bookDetail = optionalBookDetail.get();
            bookDetail.setBookDetailStatus(status);
            bookDetail.setUpdatedBy(currentUser.getUsername()); // Lưu người cập nhật
            bookDetail.setUpdatedAt(new Date()); // Lưu thời gian cập nhật
            bookDetailRepository.save(bookDetail);
            model.addAttribute("message", "Trạng thái phòng đã được cập nhật");
        } else {
            model.addAttribute("error", "Không tìm thấy chi tiết đặt phòng");
        }
        return "redirect:/search/admin/getbook?bookCode=" + bookCode;// Truyền bookCode trong URL redirect
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/book-details")
    public String getBookDetails(Model model) {
        List<Object[]> bookDetails = bookService.getBookDetails();
        model.addAttribute("bookDetails", bookDetails);
        return "export";  // Tên của Thymeleaf template
    }
    @GetMapping("/export")
    public void exportBookDetailsToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=ThongTinKhachHang.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Book Details");

        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Adult", "Email", "Check-in", "Check-out", "Children", "Price", "Total",
                "Payment Method", "Payment Status", "Booking Status", "Updated At",
                "Updated By", "Create Date", "Book Code"
        };        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        List<Object[]> bookDetails = bookService.getBookDetails();
        int rowIndex = 1;
        for (Object[] detail : bookDetails) {
            Row row = sheet.createRow(rowIndex++);
            for (int i = 0; i < detail.length; i++) {
                row.createCell(i).setCellValue(detail[i] != null ? detail[i].toString() : "");
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}

