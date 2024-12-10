package com.poly.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.poly.serviceRepository.BookingServiceRepository;
import com.poly.serviceRepository.MailerService;

import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class BookingService implements BookingServiceRepository{

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
    
    @Autowired
	MailerService mailer;
    public void bookRoom(BookingRequest request) {
    	  SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        User user = userRepository.findById(request.getUserid())
            .orElseThrow(() -> new RuntimeException("User not found with id " + request.getUserid()));

        Book book = new Book();
        book.setUser(user);
        book.setCreateDate(new Date());
        book.generateBookCode();
        Book savedBook = bookRepository.save(book);

        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Xin chào ").append(user.getName()).append(",\n\n");
        emailContent.append("Bạn đã thanh toán bằng tiền mặt. Chi tiết đặt phòng như sau:\n");
        emailContent.append("Mã đặt phòng: ").append(book.getBookCode()).append("\n");
        emailContent.append("Ngày tạo: ").append(dateFormatter.format(new Date())).append("\n\n");
        emailContent.append("Chi tiết phòng:\n");	

        for (Integer roomId : request.getRoomid()) {
            try {
                Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found with id " + roomId));
                float currentPrice = room.getCurrentPrice(new Date());
                float priceToUse = (currentPrice != room.getGia()) ? currentPrice : room.getGia();

                BookDetail bookDetail = new BookDetail();
                bookDetail.setBookid(savedBook.getId());
                bookDetail.setRoom(room);
                bookDetail.setPrice(priceToUse);
                bookDetail.setCheckin(request.getCheckin());
                bookDetail.setCheckout(request.getCheckout());

                long differenceInMillis = request.getCheckout().getTime() - request.getCheckin().getTime();
                long daysBetween = differenceInMillis / (1000 * 60 * 60 * 24);
                if (daysBetween > 7) {
                    throw new IllegalArgumentException("Thời gian đặt phòng không được vượt quá 7 ngày.");
                }
                float calculatedTotal = daysBetween * priceToUse;
                bookDetail.setTotal(calculatedTotal);
                bookDetail.setAdult(request.getAdult());
                bookDetail.setChildren(request.getChildren());
                bookDetail.setPaymentMethod(request.getPaymentMethod());
                bookDetail.setPaymentStatus(request.getPaymentStatus());
                bookDetail.setBookDetailStatus(request.getBookDetailStatus());
                bookDetailRepository.save(bookDetail);

                // Thêm thông tin phòng vào email
                emailContent.append("- Phòng: ").append(room.getSophong())
                    .append(", Giá: ").append(priceToUse)
                    .append(", Tổng tiền: ").append(calculatedTotal)
                    .append(", Checkin: ").append(dateFormatter.format(request.getCheckin()))
                    .append(", Checkout: ").append(dateFormatter.format(request.getCheckout()))
                    .append("\n");
            } catch (Exception e) {
                System.out.println("Error saving BookDetail for roomId " + roomId + ": " + e.getMessage());
            }
        }

        emailContent.append("\nCảm ơn bạn đã sử dụng dịch vụ của chúng tôi.\nNếu có bất kì vấn đề gì hãy gọi 0357184576 \nTrân trọng,\nĐội ngũ khách sạn.");

        // Gửi email
        try {
            mailer.sendEmail(
                user.getEmail(), // Người nhận
                "", // CC (nếu có)
                "Thông tin đặt phòng của bạn", // Tiêu đề email
                emailContent.toString() // Nội dung email
            );
            System.out.println("Email đã được gửi thành công.");
        } catch (Exception e) {
            System.out.println("Không thể gửi email: " + e.getMessage());
        }
    }

    
    public Book PaybookRoom(BookingRequest request) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        User user = userRepository.findById(request.getUserid())
            .orElseThrow(() -> new RuntimeException("User not found with id " + request.getUserid()));

        Book book = new Book();
        book.setUser(user);
        book.setCreateDate(new Date());
        book.generateBookCode();
        Book savedBook = bookRepository.save(book);

        float totalAmount = 0;
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("Xin chào ").append(user.getName()).append(",\n\n");
        emailContent.append("Bạn đã thanh toán bằng chuyển khoản. Chi tiết thanh toán như sau:\n");
        emailContent.append("Mã đặt phòng: ").append(book.getBookCode()).append("\n");
        emailContent.append("Ngày tạo: ").append(dateFormatter.format(new Date())).append("\n\n");
        emailContent.append("Chi tiết phòng:\n");

        for (Integer roomId : request.getRoomid()) {
            Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id " + roomId));
            float currentPrice = room.getCurrentPrice(new Date());
            
            // Kiểm tra và chọn giá phù hợp
            float priceToUse = (currentPrice != room.getGia()) ? currentPrice : room.getGia();
            BookDetail bookDetail = new BookDetail();
            bookDetail.setBookid(savedBook.getId());
            bookDetail.setRoom(room);
            bookDetail.setPrice(priceToUse);
            bookDetail.setCheckin(request.getCheckin());
            bookDetail.setCheckout(request.getCheckout());

            long differenceInMillis = request.getCheckout().getTime() - request.getCheckin().getTime();
            long daysBetween = differenceInMillis / (1000 * 60 * 60 * 24);
            float calculatedTotal = daysBetween * priceToUse;
            bookDetail.setTotal(calculatedTotal);
            totalAmount += calculatedTotal;
            bookDetail.setAdult(request.getAdult());
            bookDetail.setChildren(request.getChildren());
            bookDetail.setPaymentMethod(request.getPaymentMethod());
            bookDetail.setPaymentStatus(request.getPaymentStatus());
            bookDetail.setBookDetailStatus(request.getBookDetailStatus());
            bookDetailRepository.save(bookDetail);

            // Thêm thông tin chi tiết phòng vào nội dung email
            emailContent.append("- Phòng: ").append(room.getSophong())
                .append(", Giá: ").append(priceToUse)
                .append(", Tổng tiền: ").append(calculatedTotal)
                .append(", Checkin: ").append(dateFormatter.format(request.getCheckin()))
                .append(", Checkout: ").append(dateFormatter.format(request.getCheckout()))
                .append("\n");
        }

        emailContent.append("\nTổng thanh toán: ").append(totalAmount).append(" VND\n");
        emailContent.append("Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.\nTrân trọng,\nĐội ngũ khách sạn.");

        // Gửi email
        try {
            mailer.sendEmail(
                user.getEmail(), // Người nhận
                "", // CC (nếu có)
                "Xác nhận thanh toán đặt phòng", // Tiêu đề email
                emailContent.toString() // Nội dung email
            );
            System.out.println("Email đã được gửi thành công.");
        } catch (Exception e) {
            System.out.println("Không thể gửi email: " + e.getMessage());
        }

        return savedBook;
    }

//    public BookingResponse bookRoom(BookingRequest request) {
//        User user = userRepository.findById(request.getUserid())
//            .orElseThrow(() -> new RuntimeException("User not found with id " + request.getUserid()));
//
//        Book book = new Book();
//        book.setUser(user);
//        book.setCreateDate(new Date());
//        book.generateBookCode();
//        Book savedBook = bookRepository.save(book);
//
//        List<BookDetailResponse> roomDetails = new ArrayList<>();
//        
//        for (Integer roomId : request.getRoomid()) {
//            try {
//                Room room = roomRepository.findById(roomId)
//                    .orElseThrow(() -> new RuntimeException("Room not found with id " + roomId));
//
//                BookDetail bookDetail = new BookDetail();
//                bookDetail.setBookid(savedBook.getId());
//                bookDetail.setRoomid(room.getId());
//                bookDetail.setPrice(room.getGia());
//                bookDetail.setCheckin(request.getCheckin());
//                bookDetail.setCheckout(request.getCheckout());
//
//                long daysBetween = (request.getCheckout().getTime() - request.getCheckin().getTime()) / (1000 * 60 * 60 * 24); 
//                float calculatedTotal = daysBetween * room.getGia();
//                bookDetail.setTotal(calculatedTotal);
//                bookDetail.setAdult(request.getAdult());
//                bookDetail.setChildren(request.getChildren());
//                bookDetail.setPaymentMethod(request.getPaymentMethod());
//                bookDetail.setPaymentStatus(request.getPaymentStatus());
//
//                bookDetailRepository.save(bookDetail);
//
//                // Thêm BookDetailResponse vào danh sách
//                BookDetailResponse bookDetailResponse = new BookDetailResponse();
//                bookDetailResponse.setRoomId(room.getId());
//                bookDetailResponse.setPrice(room.getGia());
//                bookDetailResponse.setCheckin(request.getCheckin());
//                bookDetailResponse.setCheckout(request.getCheckout());
//                bookDetailResponse.setTotal(calculatedTotal);
//                bookDetailResponse.setAdult(request.getAdult());
//                bookDetailResponse.setChildren(request.getChildren());
//                bookDetailResponse.setPaymentMethod(request.getPaymentMethod());
//                bookDetailResponse.setPaymentStatus(request.getPaymentStatus());
//
//                roomDetails.add(bookDetailResponse);
//                
//            } catch (Exception e) {
//                System.out.println("Error saving BookDetail for roomId " + roomId + ": " + e.getMessage());
//            }
//        }
//
//        // Tạo BookingResponse trả về
//        BookingResponse response = new BookingResponse();
//        response.setUserEmail(user.getEmail());
//        response.setUserName(user.getName());
//        response.setRoomDetails(roomDetails);
//        
//        return response;
//    }
    public List<Book> getBooksByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user != null ? user.getBooks() : null;
    }
    public List<Object[]> getBookDetails() {
        return bookDetailRepository.findAll().stream().map(bookDetail -> {
            User user = bookDetail.getRoom().getUser();
            return new Object[] {
                   
                    user != null ? user.getEmail() : null,
                    bookDetail.getCheckin(),
                    bookDetail.getCheckout(),
                
                    bookDetail.getPrice(),
                    bookDetail.getTotal(),
                    bookDetail.getPaymentMethod(),
                    bookDetail.getPaymentStatus(),
                    bookDetail.getBookDetailStatus(),
                    bookDetail.getUpdatedAt(),
                    bookDetail.getUpdatedBy(),
                    user != null ? user.getName() : null,
                    user != null ? user.getPhone() : null
            };
        }).collect(Collectors.toList());
    }

}
