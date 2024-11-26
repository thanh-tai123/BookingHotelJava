package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.BookingRequest;
import com.poly.entity.Book;
import com.poly.entity.BookDetail;
import com.poly.repository.BookDetailRepository;
import com.poly.repository.BookRepository;
import com.poly.service.BookingService;

import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

@RestController
@RequestMapping("/payment")



public class PaymentOSController {

	
	@Value("${PAYOS_CLIENT_ID}")
	String clientId;
	
	@Value("${PAYOS_API_KEY}")
    String apiKey;
	
	@Value("${PAYOS_CHECKSUM_KEY}")
    String checksumKey;
	
	 private final BookingService bookingService;
	    private final BookRepository bookRepository;
	    private final BookDetailRepository bookDetailRepository;

	    public PaymentOSController(BookingService bookingService, BookRepository bookRepository, BookDetailRepository bookDetailRepository) {
	        this.bookingService = bookingService;
	        this.bookRepository = bookRepository;
	        this.bookDetailRepository = bookDetailRepository;
	    }
	    @PostMapping("/book-room")
	    public ResponseEntity<String> bookRoom(@RequestBody BookingRequest request) {
	        try {
	            // Đặt phòng
	            Book book = bookingService.PaybookRoom(request);

	            // Tính tổng số tiền từ BookDetail
	            List<BookDetail> bookDetails = bookDetailRepository.findByBookid(book.getId());
	            int totalAmount = 0;
	            for (BookDetail bookDetail : bookDetails) {
	                totalAmount += bookDetail.getTotal();
	            }

	            // Tạo liên kết thanh toán nếu phương thức thanh toán là 'transfer'
	            if (request.getPaymentMethod().equalsIgnoreCase("transfer")) {
	                final PayOS payOS = new PayOS(clientId, apiKey, checksumKey);
	                String domain = "http://localhost:3000";
	                Long orderCode = System.currentTimeMillis() / 1000;
	                ItemData itemData = ItemData
	                        .builder()
	                        .name("Đặt phòng khách sạn")
	                        .quantity(1)
	                        .price(totalAmount)
	                        .build();

	                PaymentData paymentData = PaymentData
	                        .builder()
	                        .orderCode(orderCode)
	                        .amount(totalAmount)
	                        .description("Thanh toán đặt phòng")
	                        .returnUrl(domain)
	                        .cancelUrl(domain)
	                        .item(itemData)
	                        .build();

	                CheckoutResponseData result = payOS.createPaymentLink(paymentData);

	                // Tạo URL để chuyển hướng
	                String redirectUrl = String.format("paymentinfo?paymentLink=%s&qrCode=%s", result.getPaymentLinkId(), result.getQrCode());
	                return ResponseEntity.ok(redirectUrl);
	            }

	            return ResponseEntity.ok("/roomBookedSuccessfully");
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("/error?errorMessage=" + e.getMessage());
	        }
	    


	    }
//	@PostMapping
//	public ResponseEntity<?> createPaymentLink() {
//		final PayOS payOS = new PayOS(clientId, apiKey, checksumKey);
//
//		String domain = "http://localhost:3000";
//        Long orderCode = System.currentTimeMillis() / 1000;
//        ItemData itemData = ItemData
//                .builder()
//                .name("Mỳ tôm Hảo Hảo ly")
//                .quantity(1)
//                .price(2000)
//                .build();
//
//        PaymentData paymentData = PaymentData
//                .builder()
//                .orderCode(orderCode)
//                .amount(2000)
//                .description("Thanh toán đơn hàng")
//                .returnUrl(domain)
//                .cancelUrl(domain)
//                .item(itemData)
//                .build();
//
//        CheckoutResponseData result = null;
//		try {
//			result = payOS.createPaymentLink(paymentData);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        return ResponseEntity.ok(result);
//        
//	}
	
}
