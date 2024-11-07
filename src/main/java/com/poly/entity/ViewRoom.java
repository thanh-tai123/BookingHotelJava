package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "view_room")
public class ViewRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
//    private Integer roomId;
    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;
    private Integer visitCount = 0; // Khởi tạo giá trị mặc định
    private Date visitDate; // Ngày truy cập

  
}


