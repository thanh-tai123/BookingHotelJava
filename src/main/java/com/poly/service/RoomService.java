package com.poly.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.poly.entity.ViewRoom;
import com.poly.repository.ViewRoomRepository;
import com.poly.serviceRepository.RoomServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dto.HotelDTO;
import com.poly.dto.RoomDTO;
import com.poly.dto.RoomRequest;
import com.poly.dto.RoomStatisticsDTO;
import com.poly.dto.RoomTypeCountDTO;
import com.poly.dto.RoomTypeDTO;
import com.poly.dto.UserDTO;
import com.poly.entity.BookDetail;
import com.poly.entity.Hotel;
import com.poly.entity.Room;
import com.poly.entity.RoomImages;
import com.poly.entity.RoomType;
import com.poly.entity.User;
import com.poly.repository.BookDetailRepository;
import com.poly.repository.HotelRepository;
import com.poly.repository.RoomRepository;
import com.poly.repository.RoomTypeRepository;
import com.poly.repository.UserRepo;
import com.poly.util._enum.RoomStatus;

@Service
public class RoomService implements RoomServiceRepository{
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
    @Autowired
    private UserRepo userRepository;
    @Autowired
    private BookDetailRepository bookDetailRepository;
    public Room findById(int id) {
        return roomRepository.findById(id).orElse(null);
    }
    public void updateRoom(int roomId, RoomRequest roomRequest, MultipartFile img, List<MultipartFile> images) {
        try {
            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Room not found"));
            
            Hotel hotel = hotelRepository.findById(roomRequest.getHotelid())
                    .orElseThrow(() -> new RuntimeException("Hotel not found"));
            RoomType roomtype = roomtypeRepository.findById(roomRequest.getRoomtypeid())
                    .orElseThrow(() -> new RuntimeException("RoomType not found"));
            User user = userRepository.findById(roomRequest.getStaffid())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (img != null && !img.isEmpty()) {
                String imageUrl = awsS3Service.saveImageToS3(img);
                
                room.setImg(imageUrl);
            }

            room.setHotel(hotel);
            room.setSophong(roomRequest.getSophong());
            room.setGia(roomRequest.getGia());
            room.setMota(roomRequest.getMota());
            room.setStatus(RoomStatus.FALSE);
            room.setUser(user);
            room.setRoomtype(roomtype);

            if (images != null && !images.isEmpty()) {
                for (MultipartFile additionalImg : images) {
                    String additionalImageUrl = awsS3Service.saveImageToS3(additionalImg);
                    RoomImages roomImage = new RoomImages();
                    roomImage.setRoom(room);
                    roomImage.setImagePath(additionalImageUrl);
                    room.getRoomImages().add(roomImage);
                }
            }

            roomRepository.save(room);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating room: " + e.getMessage());
        }
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
    public void addRoom(RoomRequest roomRequest, List<MultipartFile> images) {
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelid())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        RoomType roomtype = roomtypeRepository.findById(roomRequest.getRoomtypeid())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));
        
        User user = userRepository.findById(roomRequest.getStaffid())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = new Room();
        room.setHotel(hotel);
        room.setImg(awsS3Service.saveImageToS3(images.get(0))); // Save the first image as main image
        room.generateRoomCode();
        room.setSophong(roomRequest.getSophong());
        room.setGia(roomRequest.getGia());
        room.setMota(roomRequest.getMota());
        room.setStatus(roomRequest.getStatus());
        room.setUser(user);
        room.setRoomtype(roomtype);

        // Save additional images
        for (MultipartFile img : images) {
            String imageUrl = awsS3Service.saveImageToS3(img);
            RoomImages roomImage = new RoomImages();
            roomImage.setRoom(room);
            roomImage.setImagePath(imageUrl);
            room.getRoomImages().add(roomImage);
        }

        roomRepository.save(room);
    }
    public List<RoomDTO> getRoomsByStatus(RoomStatus status) {
        List<RoomDTO> roomDTOs = new ArrayList<>();
        List<Room> rooms = roomRepository.findByStatus(status);

        for (Room room : rooms) {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setId(room.getId());
            roomDTO.setRoomCode(room.getRoomCode());
            roomDTO.setImg(room.getImg());
            roomDTO.setSophong(room.getSophong());
            roomDTO.setGia(room.getGia());
            roomDTO.setMota(room.getMota());
            roomDTO.setStatus(room.getStatus());
            roomDTO.setNote(room.getNote());
           

            // Fetch hotel details
            Hotel hotel = room.getHotel();
            if (hotel != null) {
                HotelDTO hotelDTO = new HotelDTO();
                hotelDTO.setId(hotel.getId());
                hotelDTO.setChinhanh(hotel.getChinhanh());
                hotelDTO.setDiachi(hotel.getDiachi());
                roomDTO.setHotelid(hotelDTO); // Set the hotel details in RoomDTO
            }
            
            User user = room.getUser();
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setName(user.getName());
                userDTO.setEmail(user.getEmail());
                roomDTO.setStaffid(userDTO);
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
    public RoomDTO getRoomDetails(int roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            RoomDTO roomDTO = RoomDTO.builder()
                .id(room.getId())
                .roomCode(room.getRoomCode())
                .img(room.getImg())
                .sophong(room.getSophong())
                .gia(room.getGia())
                .mota(room.getMota())
                .status(room.getStatus())
                .note(room.getNote())
                .hotelid(HotelDTO.builder()
                        .id(room.getHotel().getId())
                        .chinhanh(room.getHotel().getChinhanh())
                        .diachi(room.getHotel().getDiachi())
                        .build())
                .roomType(RoomTypeDTO.builder()
                        .id(room.getRoomtype().getId())
                        .name(room.getRoomtype().getName())
                        .description(room.getRoomtype().getDescription())
                        .build())
                .build();
            return roomDTO;
        }
        return null;
    }
    public List<RoomDTO> getAvailableRooms(Date checkin, Date checkout, RoomStatus status) {
        // Tìm các booking có xung đột với checkin và checkout
        List<BookDetail> conflictingBookings = bookDetailRepository.findAllByCheckinLessThanEqualAndCheckoutGreaterThanEqual(checkout, checkin);

        // Lấy danh sách các phòng bận
        List<Integer> busyRoomIds = conflictingBookings.stream()
                .map(bookDetail -> bookDetail.getRoom().getId())
                .collect(Collectors.toList());

        // Lọc các phòng trống
        List<Room> availableRooms = roomRepository.findAvailableRoomsExcludingIds(busyRoomIds, status);

        // Chuyển đổi sang RoomDTO
        return availableRooms.stream()
                .map(room -> RoomDTO.builder()
                        .id(room.getId())
                        .roomCode(room.getRoomCode())
                        .img(room.getImg())
                        .sophong(room.getSophong())
                        .gia(room.getGia())
                        .mota(room.getMota())
                        .status(room.getStatus())
                        .note(room.getNote())
                        // Thêm thông tin khách sạn
                        .hotelid(HotelDTO.builder()
                                .id(room.getHotel().getId())
                                .chinhanh(room.getHotel().getChinhanh())
                                .diachi(room.getHotel().getDiachi())
                                .build())
                        // Thêm thông tin loại phòng
                        .roomType(RoomTypeDTO.builder()
                                .id(room.getRoomtype().getId())
                                .name(room.getRoomtype().getName())
                                .description(room.getRoomtype().getDescription())
                                .build())
                        .build())
                .collect(Collectors.toList());
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
            ViewRoom newVisit = new ViewRoom();
            newVisit.setUserId(userId);
            newVisit.setRoomId(roomId);
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

    public Map<String, Long> getRoomTypeCounts() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream()
                .collect(Collectors.groupingBy(room -> room.getRoomtype().getName(), Collectors.counting()));
    }
    

    public List<RoomStatisticsDTO> getRoomStatistics() {
        List<Object[]> result = roomRepository.countRoomsByBranch();
        List<RoomStatisticsDTO> roomStatusCount = new ArrayList<>();
        for (Object[] row : result) {
            roomStatusCount.add(new RoomStatisticsDTO((String) row[0], (Long) row[1]));
        }
        return roomStatusCount;
    }


}