package com.poly.controller;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import com.poly.serviceRepository.MailerService;

@Controller
@RequestMapping("/search")
@Lazy
public class BookController {
    @Autowired
    private BookRepository bookRepository; // Giả sử đây là repository của bạn cho Book
    @Autowired
    private BookDetailRepository bookDetailRepository;
    @Autowired
    private BookingService bookService;
    @Autowired
	MailerService mailer;
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
      
        return "account/searchcode"; // Tên của view Thymeleaf
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
        return "account/searchBookCode"; // Tên của view hiển thị thông tin Book
    }

    @PostMapping("/book")
    public String getBookInfo(@RequestParam("bookCode") String bookCode, Model model) {
        Book book = bookRepository.findByBookCode(bookCode);
        List<BookDetail> details = bookDetailRepository.findByBook_BookCode(bookCode); // Tìm Book theo bookCode
        if (book != null) {
            model.addAttribute("book", book);
            model.addAttribute("bookDetails", details);
        } else {
            model.addAttribute("error", "Không tìm thấy Book với BookCode: " + bookCode);
            return "account/searchcode";
        }
        model.addAttribute("bookCode", bookCode); // Đảm bảo bookCode có trong model khi cần
        return "account/searchBookCode"; // Tên của view hiển thị thông tin Book
    }

    @GetMapping("/user/books")
    public String getUserBooks(@RequestParam("Userid") Long userId, Model model) {
        List<Book> books = bookService.getBooksByUserId(userId);
        model.addAttribute("books", books);
        return "account/userBooks"; // Thymeleaf template name
    }

    @GetMapping("/bookdetail")
    public String showBookDetail() {
        return "bookdetail"; // Tên của view Thymeleaf
    }


    /* -----ADMIN, STAFF UPDATE BOOKDETAILSTATUS */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
    @GetMapping("/admin/bookcode")
    public String showAdminBookForm(Model model) {
       
        return "admin/searchcode"; // Tên của view Thymeleaf
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
            return "admin/searchcode";
        }
        model.addAttribute("bookCode", bookCode);
        return "admin/searchBookCode"; // Tên của view hiển thị thông tin Book
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('STAFF')")
    @PostMapping("/admin/book")
    public String getAdminBookInfo(@RequestParam("bookCode") String bookCode, Model model) {
        Book book = bookRepository.findByBookCode(bookCode);
        List<BookDetail> details = bookDetailRepository.findByBook_BookCode(bookCode);// Tìm Book theo bookCode
        if (book != null) {
            model.addAttribute("book", book);
            model.addAttribute("bookDetails", details);
        } else {
            model.addAttribute("error", "Không tìm thấy Book với BookCode: " + bookCode);
            return "admin/searchcode";
        }
        model.addAttribute("bookCode", bookCode);
        return "admin/searchBookCode"; // Tên của view hiển thị thông tin Book
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/updateAllStatuses")
    public String updateAllStatuses(@RequestParam Map<String, String> allParams,
                                     @RequestParam("bookCode") String bookCode,
                                     @AuthenticationPrincipal UserDetails currentUser,
                                     Model model) {
        StringBuilder emailContent = new StringBuilder(); // To accumulate email content
        String userEmail = ""; // Variable to store user email

        // Lặp qua từng phòng và cập nhật trạng thái
        for (String key : allParams.keySet()) {
            if (key.startsWith("roomStatus[")) {
                Integer detailId = Integer.parseInt(key.substring(11, key.length() - 1));
                String status = allParams.get(key);
                String paymentStatus = allParams.get("paymentStatus[" + detailId + "]");

                Optional<BookDetail> optionalBookDetail = bookDetailRepository.findById(detailId);
                if (optionalBookDetail.isPresent()) {
                    BookDetail bookDetail = optionalBookDetail.get();
                    
                    // Get user email if not already set
                    if (userEmail.isEmpty()) {
                        userEmail = bookDetail.getBook().getUser().getEmail();
                    }

                    bookDetail.setBookDetailStatus(status);
                    bookDetail.setPaymentStatus(paymentStatus);
                    bookDetail.setUpdatedBy(currentUser.getUsername());
                    bookDetail.setUpdatedAt(new Date());
                    bookDetailRepository.save(bookDetail);

                    // Append details to email content
                    emailContent.append("Chi tiết phòng đã đặt:\n")
                            .append("Số phòng: ").append(bookDetail.getRoom().getSophong()).append("\n")
                            .append("Tổng: ").append(bookDetail.getTotal()).append("\n")
                            .append("Giá: ").append(bookDetail.getPrice()).append("\n")
                            .append("Checkin: ").append(bookDetail.getCheckin()).append("\n")
                            .append("Checkout: ").append(bookDetail.getCheckout()).append("\n")
                            .append("Người lớn: ").append(bookDetail.getAdult()).append("\n")
                            .append("Trẻ em: ").append(bookDetail.getChildren()).append("\n")
                            .append("Phương thức thanh toán: ").append(bookDetail.getPaymentMethod()).append("\n")
                            .append("Trạng thái thanh toán: ").append(paymentStatus).append("\n")
                            .append("Trạng thái phòng: ").append(status).append("\n")
                            .append("Người cập nhật: ").append(currentUser.getUsername()).append("\n")
                            .append("Thời gian cập nhật: ").append(bookDetail.getUpdatedAt()).append("\n\n");
                }
            }
        }
        
        // Gửi email một lần cho tất cả các phòng
        if (emailContent.length() > 0) {
            String finalEmailContent = "Thông tin cập nhật cho Book Code: " + bookCode + "\n\n" + emailContent.toString();
            try {
                mailer.sendEmail(
                    userEmail, // Email người dùng
                    "", // CC (có thể để trống)
                    "Thông tin cập nhật đặt phòng", // Tiêu đề
                    finalEmailContent // Nội dung
                );
                model.addAttribute("message", "Trạng thái đã được cập nhật và email đã gửi thành công.");
            } catch (Exception e) {
                model.addAttribute("error", "Trạng thái đã được cập nhật nhưng không thể gửi email: " + e.getMessage());
            }
        } else {
            model.addAttribute("message", "Không có thay đổi nào để cập nhật.");
        }

        return "redirect:/search/admin/getbook?bookCode=" + bookCode;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/book-details")
    public String getBookDetails(Model model) {
        List<Object[]> bookDetails = bookService.getBookDetails();
        model.addAttribute("bookDetails", bookDetails);
        return "admin/export";  // Tên của Thymeleaf template
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

