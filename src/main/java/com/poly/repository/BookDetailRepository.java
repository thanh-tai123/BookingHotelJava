package com.poly.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	    @Query("SELECT bd FROM BookDetail bd JOIN FETCH bd.room WHERE bd.room = :room")
	    List<BookDetail> findByRoom(@Param("room") Room room);

//	    @Query("SELECT b FROM BookDetail b WHERE b.checkout >= :checkin AND b.checkin < :checkout")
//	    List<BookDetail> findAllByCheckoutGreaterThanEqualAndCheckinLessThan(@Param("checkin") Date checkin, @Param("checkout") Date checkout);
	    @Query("SELECT b FROM BookDetail b WHERE " +
	    	       "((b.checkin < :checkout AND b.checkout > :checkin) OR " +
	    	       "(b.checkin >= :checkin AND b.checkout <= :checkout)) AND " +
	    	       "b.bookDetailStatus NOT IN ('cancel', 'checkout')")
	    	List<BookDetail> findAllConflictingBookings(@Param("checkin") Date checkin, @Param("checkout") Date checkout);


	    @Query("SELECT b.user.email, COUNT(bd.id) " +
	            "FROM BookDetail bd " +
	            "JOIN bd.book b " +
	            "WHERE YEAR(bd.checkout) = :year AND MONTH(bd.checkout) = :month AND bd.bookDetailStatus = 'checkout'" +
	            "GROUP BY b.user.email " +
	            "ORDER BY COUNT(bd.id) DESC")
	     List<Object[]> findTopUsersByMonthAndYear(@Param("year") int year, @Param("month") int month);
	     
	     @Query("SELECT h.chinhanh AS branch, SUM(bd.price) AS revenue " +
	             "FROM BookDetail bd " +
	             "JOIN bd.room r " +
	             "JOIN r.hotel h " +
	             "WHERE FUNCTION('YEAR', bd.checkout) = :year AND bd.bookDetailStatus = 'checkout'" +
	             "GROUP BY h.chinhanh")
	      List<Map<String, Object>> findRevenueByBranchAndYear(@Param("year") Integer year);
	     
	     @Query("SELECT bd.room.id AS roomId, r.roomCode AS roomCode, r.img AS img, rt.name AS roomType, " +
             "r.gia AS price, h.chinhanh AS branch " +
             "FROM BookDetail bd " +
             "JOIN bd.room r " +
	             "JOIN r.roomtype rt " +
	             "JOIN r.hotel h " +
             "WHERE h.chinhanh = :branchName AND bd.bookDetailStatus = 'checkout'")
      List<Map<String, Object>> findRoomsBookedByBranch(@Param("branchName") String branchName);
	     

}