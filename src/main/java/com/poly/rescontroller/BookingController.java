package com.poly.rescontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.BookDetailDateDTO;
import com.poly.dto.BookingRequest;
import com.poly.dto.BookingResponse;
import com.poly.dto.RoomRequest;
import com.poly.entity.BookDetail;
import com.poly.entity.MailInfo;
import com.poly.entity.Room;
import com.poly.repository.BookDetailRepository;
import com.poly.service.BookDetailService;
import com.poly.service.BookingService;
import com.poly.service.UserService;
import com.poly.serviceRepository.MailerService;

@RestController
@RequestMapping("/api")
public class BookingController {
	  @Autowired 
	    private UserService userService;
	  @Autowired
	  private MailerService mailer;  // MailerService là loại của mailer, thay đổi nếu cần

    @Autowired 
    private BookingService bookingService;
    @Autowired 
    private BookDetailRepository bookDetailRepository;
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
    public List<BookDetailDateDTO> getBookings(@RequestParam("roomId") Room roomId) {
        return bookdetail.getBookingsByRoomid(roomId);
    }

}

