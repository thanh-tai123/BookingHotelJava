package com.poly.rescontroller;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.poly.dto.BookingRequest;
import com.poly.dto.RoomRequest;
import com.poly.entity.BookDetail;
import com.poly.entity.BookResponse;
import com.poly.entity.Room;
import com.poly.repository.BookDetailRepository;
import com.poly.repository.RoomRepository;
import com.poly.service.RoomService;
import com.poly.util._enum.RoomStatus;

@RestController
@RequestMapping("/api")
public class RoomRestController {
	@Autowired
    private RoomService roomService;
	 @Autowired
	    private RoomRepository roomRepository;
	 @Autowired
	    private BookDetailRepository bookDetailRepository;

	 
	  @GetMapping("/get-room-details")
	    public Room getRoomDetails(@RequestParam int roomId) {
	        return roomRepository.findById(roomId).orElse(null);
	    }
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable int id) {
        Room room = roomService.findById(id);
        if (room != null) {
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/{roomId}/book")
    public ResponseEntity<String> bookRoom(@PathVariable int roomId) {
        // Logic to book the room
        return ResponseEntity.ok("Room booked successfully!");
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Room>> getRoomsByStatus(@PathVariable RoomStatus status) {
        List<Room> rooms = roomService.getRoomsByStatus(status);
        return ResponseEntity.ok(rooms);
    }
//    @PostMapping("/add-room")
//    public ResponseEntity<String> addRoom(@RequestBody RoomRequest roomRequest) {
//        roomService.addRoom(roomRequest);
//        return ResponseEntity.ok("Room added successfully");
//    }

    @PostMapping("/add-room")
    public ResponseEntity<String> addRoom(@ModelAttribute RoomRequest roomRequest, 
                                          @RequestParam("img") MultipartFile img) {
        roomService.addRoom(roomRequest, img);
        
        return ResponseEntity.ok("{\"message\":\"Room added successfully\"}");
    }

//    @PostMapping("/temp-book-room")
//    public ResponseEntity<?> tempBookRoom(@RequestBody BookingRequest request) {
//        // Save the booking temporarily (in a temporary table or in-memory)
//        // You could use a service that saves the booking in a temporary state
//        // For example, save it in a Map or a temporary database table
//        // Return a response indicating success
//        return ResponseEntity.ok("Temporary booking saved");
//    }
//    @PostMapping("/book")
//    public ResponseEntity<BookResponse> bookRoom(@RequestParam Map<String, String> formData) {
//        // Process the form data
//        Room room = new Room();
//        room.setId(Integer.parseInt(formData.get("roomId")));
//        room.setKieuphong(formData.get("roomType"));
//        room.setGia(Float.parseFloat(formData.get("roomPrice")));
//        room.setMota(formData.get("roomDescription"));
//        // ...
//        BookResponse response = new BookResponse(room);
//        return ResponseEntity.ok(response).header("Content-Type", "application/json");
//    }
    @GetMapping("/get-available-rooms")
    public List<Room> getAvailableRooms(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin, 
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout) {
        
        // Find conflicting bookings
        List<BookDetail> conflictingBookings = bookDetailRepository.findAllByCheckinLessThanEqualAndCheckoutGreaterThanEqual(checkout, checkin);

        // Get the list of busy room IDs
        List<Integer> busyRoomIds = conflictingBookings.stream()
                .map(BookDetail::getRoomid)
                .collect(Collectors.toList());

        // Filter available rooms
        return roomRepository.findAvailableRoomsExcludingIds(busyRoomIds, RoomStatus.TRUE);
    }

    
    @PutMapping("/update-room/{roomId}")
    public ResponseEntity<Map<String, String>> updateRoom(
            @PathVariable int roomId,
            @ModelAttribute RoomRequest roomRequest, 
            @RequestParam(value = "img", required = false) MultipartFile img) {
        
        // Gọi service để thực hiện cập nhật
        roomService.updateRoom(roomId, roomRequest, img);
        
        // Trả về JSON xác nhận cập nhật thành công
        return ResponseEntity.ok(Map.of("message", "Room updated successfully"));
    }



    // API xóa phòng
    @DeleteMapping("/delete-room/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable int id) {
        try {
            boolean deleted = roomService.deleteRoom(id);
            if (deleted) {
                return ResponseEntity.ok("Room deleted successfully!");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting room: " + e.getMessage());
        }
    }
}
