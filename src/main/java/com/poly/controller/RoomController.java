package com.poly.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.poly.repository.ViewRoomRepository;
import com.poly.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Room;
import com.poly.entity.RoomTypeByService;
import com.poly.repository.ProductRepo;
import com.poly.repository.RoomRepository;
import com.poly.service.RoomService;
import com.poly.util._enum.RoomStatus;

@Controller
@RequestMapping("/room")
public class RoomController {
	 @Autowired
	  private RoomRepository roomRepo;
	  @Autowired
	    private RoomService roomService;

	@Autowired
	private ViewRoomRepository viewRoomRepository;
	@RequestMapping("")
	public String index(Model model) {
		// Fetch only rooms with status TRUE
		List<Room> rooms = roomRepo.findByStatus(RoomStatus.TRUE);

		// Tạo một Map để lưu số lần truy cập cho từng phòng
		Map<Integer, Integer> visitCounts = new HashMap<>();

		// Lặp qua danh sách phòng để lấy số lượt truy cập
		for (Room room : rooms) {
			int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
			visitCounts.put(room.getId(), visitCount);
		}

		// Thêm danh sách phòng và số lượt truy cập vào model
		model.addAttribute("rooms", rooms);
		model.addAttribute("visitCounts", visitCounts); // Thêm số lần truy cập vào model

		return "room"; // Trả về mẫu Thymeleaf
	}
	 @RequestMapping("/search")
	 public String searchAvailableRooms(
	     @RequestParam int hotelId, 
	     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin, 
	     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout,
	     Model model) {
	     
		 List<Room> availableRooms = roomRepo.findAvailableRooms(hotelId, checkin, checkout, RoomStatus.TRUE);

	     
	     model.addAttribute("rooms", availableRooms);
	     return "room";
	 }

	@RequestMapping("/{id}")
	public String detail(@PathVariable int id, Model model) {
		Integer userId = getCurrentUserId();

		Room room = roomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + id));

		List<RoomTypeByService> services = room.getRoomtype().getServices();

		roomService.viewRoomDetails(id, userId);

		int totalVisits = roomService.getVisitCount(id);

		// Thêm thông tin vào model
		model.addAttribute("room", room);
		model.addAttribute("services", services);
		model.addAttribute("visitCount", totalVisits);
		return "roomdetailversion"; // Trả về mẫu Thymeleaf
	}

	private Integer getCurrentUserId() {
		return 1;
	}


	/*
		 * @GetMapping(value = {"/book-room/**"}) public String index() { return "book";
		 * // This should point to your AngularJS app's main HTML file }
		 */

	  @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN') or hasAuthority('SUPPLIER') or hasAuthority('MOD')")
	    @GetMapping("/room-details/{roomId}")
	    public String showRoomDetails(@PathVariable Integer roomId, Model model) {
	        model.addAttribute("roomId", roomId);
	        return "book";
	    }
	    @RequestMapping("displaypayment")
	    public String display(Model model) {
	        // Fetch only rooms with status TRUE
	       
	        return "display";
	    }
	    @GetMapping("/filter")
	    public String listRooms(@RequestParam(required = false) String roomtype, Model model) {
	        List<Room> rooms;
	        if (roomtype != null && !roomtype.isEmpty()) {
	            rooms = roomService.findByRoomType(roomtype);
	        } else {
	            rooms = roomService.findAll();
	        }

	        // Tạo một Map để lưu số lần truy cập cho từng phòng
	        Map<Integer, Integer> visitCounts = new HashMap<>();

	        // Lặp qua danh sách phòng để lấy số lượt truy cập
	        for (Room room : rooms) {
	            int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
	            visitCounts.put(room.getId(), visitCount);
	        }

	        // Thêm danh sách phòng và số lượt truy cập vào model
	        model.addAttribute("rooms", rooms);
	        model.addAttribute("visitCounts", visitCounts); // Thêm số lần truy cập vào model

	        return "room"; // Trả về mẫu Thymeleaf
	    }
}
