package com.poly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dto.RoomRequest;
import com.poly.entity.Hotel;
import com.poly.entity.Room;
import com.poly.entity.RoomType;
import com.poly.repository.HotelRepository;
import com.poly.repository.RoomRepository;
import com.poly.repository.RoomTypeRepository;
import com.poly.util._enum.RoomStatus;

@Service
public class RoomService {
	 @Autowired
	    private HotelRepository hotelRepository;
	 @Autowired
	    private RoomTypeRepository roomtypeRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private AwsS3Service awsS3Service;

    public Room findById(int id) {
        return roomRepository.findById(id).orElse(null);
    }
    public void updateRoom(int roomId, RoomRequest roomRequest, MultipartFile img) {
        // Tìm room cần update
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        
        // Tìm hotel và roomtype liên quan
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelid())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        RoomType roomtype = roomtypeRepository.findById(roomRequest.getRoomtypeid())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        // Cập nhật ảnh nếu có ảnh mới
        if (img != null && !img.isEmpty()) {
            String imageUrl = awsS3Service.saveImageToS3(img);
            room.setImg(imageUrl);
        }

        // Cập nhật các thuộc tính khác của room
        room.setHotel(hotel);
        room.setSophong(roomRequest.getSophong());
        room.setKieuphong(roomRequest.getKieuphong());
        room.setGia(roomRequest.getGia());
        room.setMota(roomRequest.getMota());
        room.setStatus(roomRequest.getStatus());
        // room.setNote(roomRequest.getNote()); // nếu có trường ghi chú
        room.setStaffid(roomRequest.getStaffid());
        room.setRoomtype(roomtype);

        // Lưu lại thông tin phòng sau khi cập nhật
        roomRepository.save(room);
    }


    // Xóa phòng
    public boolean deleteRoom(int id) {
        if (roomRepository.existsById(id)) { // Kiểm tra xem phòng có tồn tại không
            roomRepository.deleteById(id); // Xóa phòng
            return true;
        }
        return false; // Phòng không tồn tại
    }
    public List<Room> getRoomsByStatus(RoomStatus status) {
        return roomRepository.findByStatus(status);
    }
    public void addRoom(RoomRequest roomRequest, MultipartFile img) {
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelid())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        RoomType roomtype = roomtypeRepository.findById(roomRequest.getRoomtypeid())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
        String imageUrl = awsS3Service.saveImageToS3(img);
        Room room = new Room();
        room.setHotel(hotel);
        room.setImg(imageUrl);
        room.setSophong(roomRequest.getSophong());
        room.setKieuphong(roomRequest.getKieuphong());
        room.setGia(roomRequest.getGia());
        room.setMota(roomRequest.getMota());
        room.setStatus(roomRequest.getStatus());
       // room.setNote(roomRequest.getNote());
        room.setStaffid(roomRequest.getStaffid());
        room.setRoomtype(roomtype);
        roomRepository.save(room);
    }
}