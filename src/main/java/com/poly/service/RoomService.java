package com.poly.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.poly.entity.ViewRoom;
import com.poly.repository.ViewRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dto.HotelDTO;
import com.poly.dto.RoomDTO;
import com.poly.dto.RoomRequest;
import com.poly.dto.RoomTypeDTO;
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
    @Autowired
    private ViewRoomRepository viewRoomRepository;

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
//    public List<Room> getRoomsByStatus(RoomStatus status) {
//        return roomRepository.findByStatus(status);
//    }
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
    
    public List<RoomDTO> getRoomsByStatus(RoomStatus status) {
        List<RoomDTO> roomDTOs = new ArrayList<>();
        List<Room> rooms = roomRepository.findByStatus(status);

        for (Room room : rooms) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(room.getId());
         
            roomDTO.setImg(room.getImg());
            roomDTO.setSophong(room.getSophong());
            roomDTO.setGia(room.getGia());
            roomDTO.setMota(room.getMota());
            roomDTO.setStatus(room.getStatus());
            roomDTO.setNote(room.getNote());
            roomDTO.setStaffid(room.getStaffid());

            // Fetch hotel details
            Hotel hotel = room.getHotel();
            if (hotel != null) {
                HotelDTO hotelDTO = new HotelDTO();
                hotelDTO.setId(hotel.getId());
                hotelDTO.setChinhanh(hotel.getChinhanh());
                hotelDTO.setDiachi(hotel.getDiachi());
                roomDTO.setHotelid(hotelDTO); // Set the hotel details in RoomDTO
            }

            // Fetch room type details
            RoomType roomType = room.getRoomtype();
            if (roomType != null) {
                RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
                roomTypeDTO.setId(roomType.getId());
                roomTypeDTO.setName(roomType.getName());
                roomDTO.setRoomType(roomTypeDTO);
            }

            roomDTOs.add(roomDTO);
        }

        return roomDTOs;
    }
    
    public List<Room> findByRoomType(String roomtype) {
        return roomRepository.findByRoomtype_Name(roomtype);
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public void viewRoomDetails(int roomId, Integer userId) {
        List<ViewRoom> existingVisits = viewRoomRepository.findByRoomIdAndUserId(roomId, userId);

        if (existingVisits.isEmpty()) {
            Room room = roomRepository.findById(roomId).orElseThrow(null);
            ViewRoom newVisit = new ViewRoom();
            newVisit.setUserId(userId);
            newVisit.setRoom(room);
            newVisit.setVisitCount(1);
            newVisit.setVisitDate(new Date());
            viewRoomRepository.save(newVisit);
        } else {
            viewRoomRepository.incrementVisitCount(roomId, userId);
        }
    }

    public int getVisitCount(int roomId) {
        return viewRoomRepository.getTotalVisitCountByRoomId(roomId);
    }

}