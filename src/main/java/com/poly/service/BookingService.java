package com.poly.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.BookDetailResponse;
import com.poly.dto.BookingRequest;
import com.poly.dto.BookingResponse;
import com.poly.entity.Book;
import com.poly.entity.BookDetail;
import com.poly.entity.Room;
import com.poly.entity.User;
import com.poly.repository.BookDetailRepository;
import com.poly.repository.BookRepository;
import com.poly.repository.RoomRepository;
import com.poly.repository.UserRepo;

import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class BookingService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private BookDetailRepository bookDetailRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserService userService;
    public void bookRoom(BookingRequest request) {
        User user = userRepository.findById(request.getUserid())
            .orElseThrow(() -> new RuntimeException("User not found with id " + request.getUserid()));

        Book book = new Book();
        book.setUser(user);
        book.setCreateDate(new Date());
        book.generateBookCode();
        Book savedBook = bookRepository.save(book);

        for (Integer roomId : request.getRoomid()) {
            try {
                Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found with id " + roomId));
                
                BookDetail bookDetail = new BookDetail();
                bookDetail.setBookid(savedBook.getId());
//                bookDetail.setBooklist(savedBook.getId());
                bookDetail.setRoomid(room.getId());
                bookDetail.setPrice(room.getGia());
                bookDetail.setCheckin(request.getCheckin());
                bookDetail.setCheckout(request.getCheckout());

                long differenceInMillis = request.getCheckout().getTime() - request.getCheckin().getTime();
                long daysBetween = differenceInMillis / (1000 * 60 * 60 * 24); 
                float calculatedTotal = daysBetween * room.getGia();
                bookDetail.setTotal(calculatedTotal);
                bookDetail.setAdult(request.getAdult());
                bookDetail.setChildren(request.getChildren());
                bookDetail.setPaymentMethod(request.getPaymentMethod());
                bookDetail.setPaymentStatus(request.getPaymentStatus());
                bookDetail.setBookDetailStatus(request.getBookDetailStatus());
                bookDetailRepository.save(bookDetail);
            } catch (Exception e) {
                System.out.println("Error saving BookDetail for roomId " + roomId + ": " + e.getMessage());
            }
        }

    }

    public List<Book> getBooksByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getBooks() : null;
    }
    public void updatePaymentStatus(Integer bookingId, String status) {
        BookDetail booking = bookDetailRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking ID not found"));
        
        booking.setPaymentStatus(status); // Set paymentStatus to 'success'
        bookDetailRepository.save(booking);
    }



}
