package com.poly.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.entity.BookDetail;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Integer> {
	
	 @Query("SELECT MONTH(b.checkin) AS month, SUM(b.total) AS revenue FROM BookDetail b GROUP BY MONTH(b.checkin)")
	    List<Object[]> findMonthlyRevenue();
	    
	    @Query("SELECT MONTH(b.checkin) AS month, COUNT(b.roomid) AS roomCount FROM BookDetail b GROUP BY MONTH(b.checkin)")
	    List<Object[]> findMonthlyRoomCount();
	    List<BookDetail> findByBook_BookCode(String bookCode);
	    List<BookDetail> findByRoomid(Long roomid);
	    List<BookDetail> findAllByCheckinLessThanEqualAndCheckoutGreaterThanEqual(Date checkout, Date checkin);
}