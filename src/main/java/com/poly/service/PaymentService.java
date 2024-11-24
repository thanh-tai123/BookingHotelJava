package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.PaymentInfo;
import com.poly.entity.Book;
import com.poly.repository.BookRepository;

@Service
public class PaymentService {
//	@Autowired
//	private final BookRepository bookRepository;
//
//    public PaymentService(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//
//    public PaymentInfo getPaymentInfo(String paymentLinkId) {
//        // Lấy thông tin booking dựa trên paymentLinkId (ở đây dùng bookCode để đơn giản)
//        Book bookingInfo = bookRepository.findByBookCode(paymentLinkId);
//
//        // Nếu không tìm thấy booking thì ném lỗi
//        if (bookingInfo == null) {
//            throw new RuntimeException("Booking not found for paymentLinkId: " + paymentLinkId);
//        }
//
//        // Tạo và gán các giá trị cho PaymentInfo
//        PaymentInfo paymentInfo = new PaymentInfo();
//        paymentInfo.setAmount(bookingInfo.get());
//        paymentInfo.setDescription("Thanh toan don hang");
//        paymentInfo.setOrderCode(bookingInfo.getOrderCode());
//        paymentInfo.setStatus("PENDING");
//        paymentInfo.setCheckoutUrl("https://pay.payos.vn/web/" + paymentLinkId);
//        paymentInfo.setQrCode("00020101021238570010A000000727012700069704220113VQRQAAUOC89660208QRIBFTTA5303704540420005802VN62230819Thanh toan don hang6304B28F");
//        paymentInfo.setBookingInfo(bookingInfo);
//
//        return paymentInfo;
//    }
}