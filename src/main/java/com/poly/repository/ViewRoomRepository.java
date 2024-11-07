package com.poly.repository;
import com.poly.entity.ViewRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ViewRoomRepository extends JpaRepository<ViewRoom, Integer> {
    @Query("SELECT COALESCE(SUM(v.visitCount), 0) " +
            "FROM ViewRoom v " +
            "WHERE v.roomId = :roomId")
    Integer getTotalVisitCountByRoomId(@Param("roomId") int roomId);




    @Modifying
    @Transactional
    @Query("UPDATE ViewRoom v SET v.visitCount = v.visitCount + 1, v.visitDate = CURRENT_TIMESTAMP WHERE v.roomId = :roomId AND v.userId = :userId")
    void incrementVisitCount(@Param("roomId") int roomId, @Param("userId") Integer userId);

    @Query("SELECT v FROM ViewRoom v WHERE v.roomId = :roomId AND v.userId = :userId")
    List<ViewRoom> findByRoomIdAndUserId(@Param("roomId") int roomId, @Param("userId") Integer userId);
}







