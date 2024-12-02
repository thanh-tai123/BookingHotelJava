package com.poly.serviceRepository;

import java.util.List;
import java.util.Map;

import com.poly.dto.BookDetailDateDTO;
import com.poly.entity.BookDetail;
import com.poly.entity.Room;

public interface BookDetailServiceRepository {
	 public List<Map<String, Object>> getMonthlyRevenueByYear(Integer year);
	 public List<Map<String, Object>> getTopUsersByMonthAndYear(int year, int month);
	 public List<BookDetailDateDTO> getBookingsByRoomid(Room room);
}
