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
	 @Query("SELECT YEAR(b.checkout) AS year, MONTH(b.checkout) AS month, SUM(b.total) AS revenue " +
		       "FROM BookDetail b " +
		       "WHERE YEAR(b.checkout) = :year AND b.bookDetailStatus = 'checkout' " +
		       "GROUP BY YEAR(b.checkout), MONTH(b.checkout)")
	    List<Object[]> findMonthlyRevenueByYear(@Param("year") Integer year);

	    @Query("SELECT YEAR(b.checkout) AS year, MONTH(b.checkout) AS month, COUNT(b.id) AS roomCount FROM BookDetail b WHERE YEAR(b.checkout) = :year AND b.bookDetailStatus = 'checkout'"
	    +"GROUP BY YEAR(b.checkout), MONTH(b.checkout)")
	    List<Object[]> findMonthlyRoomCountByYear(@Param("year") Integer year);
	    List<BookDetail> findByBook_BookCode(String bookCode);
	    List<BookDetail> findByRoom(Room room);
	    List<BookDetail> findAllByCheckinLessThanEqualAndCheckoutGreaterThanEqual(Date checkout, Date checkin);
	    
	    @Query("SELECT b.user.email, COUNT(bd.id) " +
	            "FROM BookDetail bd " +
	            "JOIN bd.book b " +
	            "WHERE YEAR(bd.checkout) = :year AND MONTH(bd.checkout) = :month " +
	            "GROUP BY b.user.email " +
	            "ORDER BY COUNT(bd.id) DESC")
	     List<Object[]> findTopUsersByMonthAndYear(@Param("year") int year, @Param("month") int month);
}