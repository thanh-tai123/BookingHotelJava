package com.poly.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.entity.BookDetail;
import com.poly.entity.Room;

@Repository
public interface BookDetailRepository extends JpaRepository<BookDetail, Integer> {
	 List<BookDetail> findByBookid(Integer bookId);
	 @Query("SELECT YEAR(b.checkin) AS year, MONTH(b.checkin) AS month, SUM(b.total) AS revenue " +
		       "FROM BookDetail b " +
		       "WHERE YEAR(b.checkin) = :year AND b.bookDetailStatus = 'checkout' " +
		       "GROUP BY YEAR(b.checkin), MONTH(b.checkin)")
	    List<Object[]> findMonthlyRevenueByYear(@Param("year") Integer year);

	    @Query("SELECT YEAR(b.checkin) AS year, MONTH(b.checkin) AS month, COUNT(b.id) AS roomCount FROM BookDetail b WHERE YEAR(b.checkin) = :year AND b.bookDetailStatus = 'checkout'"
	    +"GROUP BY YEAR(b.checkin), MONTH(b.checkin)")
	    List<Object[]> findMonthlyRoomCountByYear(@Param("year") Integer year);
	    List<BookDetail> findByBook_BookCode(String bookCode);
	    List<BookDetail> findByRoom(Room room);
	    List<BookDetail> findAllByCheckinLessThanEqualAndCheckoutGreaterThanEqual(Date checkout, Date checkin);
	    
	    @Query("SELECT b.user.email, COUNT(bd.id) " +
	            "FROM BookDetail bd " +
	            "JOIN bd.book b " +
	            "WHERE YEAR(bd.checkin) = :year AND MONTH(bd.checkin) = :month " +
	            "GROUP BY b.user.email " +
	            "ORDER BY COUNT(bd.id) DESC")
	     List<Object[]> findTopUsersByMonthAndYear(@Param("year") int year, @Param("month") int month);
}