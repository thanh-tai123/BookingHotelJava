package com.poly.rescontroller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.BookingRequest;
import com.poly.dto.BookingResponse;
import com.poly.dto.RoomRequest;
import com.poly.entity.BookDetail;
import com.poly.entity.MailInfo;
import com.poly.service.BookDetailService;
import com.poly.service.BookingService;
import com.poly.service.MailerService;
import com.poly.service.PayOSService;
import com.poly.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class BookingController {
	  @Autowired 
	    private UserService userService;
	  @Autowired
	  private MailerService mailer;  // MailerService là loại của mailer, thay đổi nếu cần

	    @Autowired
	    private PayOSService payOSService;
    @Autowired 
    private BookingService bookingService;
    @Autowired
    private BookDetailService bookdetail;
    @PostMapping("/book-room")
    public ResponseEntity<?> bookRoom(@RequestBody BookingRequest request) {
        bookingService.bookRoom(request);
      
        return ResponseEntity.ok("Room booked successfully");
    }
//    @PostMapping("/book-room")
//    public ResponseEntity<BookingResponse> bookRoom(@RequestBody BookingRequest request) {
//        BookingResponse bookingResponse = bookingService.bookRoom(request); // Modify to return booking details
//        return ResponseEntity.ok(bookingResponse);
//    }

    @GetMapping("/get-bookings")
    public List<BookDetail> getBookings(@RequestParam("roomId") Long roomId) {
        return bookdetail.getBookingsByRoomid(roomId);
    }
//    @PostMapping("/book-room")
//    public ResponseEntity<String> bookRoom(@RequestBody BookingRequest bookingRequest) {
//        try {
//            bookingService.bookRoom(bookingRequest);z
//
//            if ("transfer".equalsIgnoreCase(bookingRequest.getPaymentMethod())) {
//                String paymentUrl = payOSService.createPayment(bookingRequest.getTotal());
//                return ResponseEntity.ok(paymentUrl);
//            } else {
//                return ResponseEntity.ok("Booking successful with cash payment");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error booking room: " + e.getMessage());
//        }
//    }

//    @GetMapping("/pay")
//    public void pay(@RequestParam double total, HttpServletResponse response) throws IOException {
//        String paymentUrl = payOSService.createPayment(total);
//        response.sendRedirect(paymentUrl);
//    }
}

