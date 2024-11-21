package com.poly.serviceRepository;

import java.util.List;

import com.poly.dto.BookingRequest;
import com.poly.entity.Book;

public interface BookingServiceRepository {
	public void bookRoom(BookingRequest request);
	 public Book PaybookRoom(BookingRequest request);
	 public List<Book> getBooksByUserId(Long userId);
	 public List<Object[]> getBookDetails();
}
