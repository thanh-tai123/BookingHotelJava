package com.poly.controller;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.poly.entity.Hotel;
import com.poly.entity.RoomType;
import com.poly.repository.ViewRoomRepository;
import com.poly.service.HotelService;
import com.poly.service.RoomService;
import com.poly.service.RoomTypeService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Controller
@RequestMapping("/room")
public class RoomController {
	@Autowired
	private RoomRepository roomRepo;
	@Autowired
	private RoomService roomService;
	@Autowired
	private HotelService hotelService;
	@Autowired
	private ViewRoomRepository viewRoomRepository;

	@Autowired
	private RoomTypeService roomTypeService; // Dịch vụ để lấy danh sách loại phòng

	@RequestMapping("")
	public String listRooms(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
							@RequestParam(value = "sortField", defaultValue = "id") String sortField,
							@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
							@RequestParam(value = "keyword", required = false) String keyword, Model model) {

		// Tạo Pageable với các tham số phân trang và sắp xếp
		Pageable pageable = PageRequest.of(pageNum - 1, 3, Sort.by(Sort.Order.by(sortField).with(Sort.Direction.fromString(sortDir))));

		// Truy vấn phòng với trạng thái TRUE, có phân trang
		Page<Room> page = roomRepo.findByStatus(RoomStatus.TRUE, pageable);

		// Lấy danh sách các phòng từ trang
		List<Room> rooms = page.getContent();

		// Lấy số lượt truy cập cho mỗi phòng
		Map<Integer, Integer> visitCounts = new HashMap<>();
		for (Room room : rooms) {
			int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
			visitCounts.put(room.getId(), visitCount);
		}

		// Lấy các loại phòng và khách sạn
		List<RoomType> roomTypes = roomTypeService.getAllRoomType();
		List<Hotel> hotels = hotelService.getAllHotels();

		// Danh sách các khoảng giá có sẵn
		List<String> priceRanges = Arrays.asList("1000-5000", "5000-10000", "10000-20000");

		// Thêm các đối tượng vào model để hiển thị trong view
		model.addAttribute("rooms", rooms);
		model.addAttribute("visitCounts", visitCounts);
		model.addAttribute("roomTypes", roomTypes);
		model.addAttribute("branches", hotels);
		model.addAttribute("priceRanges", priceRanges);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);

		return "room";
	}

	@RequestMapping("/search")
	public String searchAvailableRooms(
			@RequestParam int hotelId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout,
			Model model) {

		List<Room> availableRooms = roomRepo.findAvailableRooms(hotelId, checkin, checkout, RoomStatus.TRUE);
		List<Hotel> branches = hotelService.getAllHotels();
		List<RoomType> roomTypes = roomTypeService.getAllRoomType();
		model.addAttribute("roomTypes", roomTypes);
		model.addAttribute("branches", branches);
		model.addAttribute("checkin", checkin);
		model.addAttribute("checkout", checkout);
		// Ví dụ dữ liệu visitCounts
		Map<Integer, Integer> visitCounts = new HashMap<>();
		for (Room room : availableRooms) {
			// Giả sử bạn có một phương thức để lấy số lượt xem của phòng
			int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
			visitCounts.put(room.getId(), visitCount);
		}

		model.addAttribute("rooms", availableRooms);
		model.addAttribute("visitCounts", visitCounts);  // Thêm visitCounts vào model

		return "room";
	}



	 @RequestMapping("/{id}")
	    public String detail(@PathVariable int id, Model model) {
	        Integer userId = getCurrentUserId();


	      
		Room room = roomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + id));
		List<RoomTypeByService> services = room.getRoomtype().getServices();

		roomService.viewRoomDetails(id, userId);
		int totalVisits = roomService.getVisitCount(id);

		model.addAttribute("room", room);
		model.addAttribute("services", services);
		model.addAttribute("visitCount", totalVisits);
		return "roomdetailversion";
	}

	private Integer getCurrentUserId() {
		// Implement this method to get the current user's ID
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
	public String listRooms(@RequestParam(required = false) String roomtype,
							@RequestParam(required = false) String priceRange,
							Model model) {
		List<Room> rooms = roomService.findAll(); // Lấy tất cả các phòng trước

		if (roomtype != null && !roomtype.isEmpty() && !roomtype.equals("Tất cả")) {
			rooms = rooms.stream()
					.filter(room -> room.getRoomtype().getName().equalsIgnoreCase(roomtype))
					.collect(Collectors.toList());
		}

		if (priceRange != null && !priceRange.isEmpty() && !priceRange.equals("Tất cả")) {
			String[] priceParts = priceRange.split("-");

			try {
				int minPrice = Integer.parseInt(priceParts[0].trim());
				int maxPrice = Integer.parseInt(priceParts[1].trim());

				// Lọc phòng theo khoảng giá
				rooms = rooms.stream()
						.filter(room -> room.getGia() >= minPrice && room.getGia() <= maxPrice)
						.collect(Collectors.toList());
			} catch (NumberFormatException e) {
				model.addAttribute("message", "Khoảng giá không hợp lệ.");
				rooms = new ArrayList<>();
			}
		}

		// Kiểm tra nếu không có phòng
		if (rooms.isEmpty()) {
			model.addAttribute("message", "Không có phòng phù hợp với các tiêu chí lọc.");
		}

		// Lấy danh sách khách sạn và loại phòng
		List<Hotel> branches = hotelService.getAllHotels();
		model.addAttribute("branches", branches);

		List<RoomType> roomTypes = roomTypeService.getAllRoomType();
		model.addAttribute("roomTypes", roomTypes);
		model.addAttribute("roomtype", roomtype);
		model.addAttribute("priceRange", priceRange); // Truyền priceRange vào model

		// Tạo một Map để lưu số lần truy cập cho từng phòng
		Map<Integer, Integer> visitCounts = new HashMap<>();
		for (Room room : rooms) {
			int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
			visitCounts.put(room.getId(), visitCount);
		}

		// Truyền danh sách các khoảng giá có thể chọn vào model
		List<String> priceRanges = Arrays.asList("1000-5000", "5000-10000", "10000-20000");
		model.addAttribute("priceRanges", priceRanges);

		model.addAttribute("rooms", rooms);
		model.addAttribute("visitCounts", visitCounts); // Thêm số lần truy cập vào model

		return "room"; // Trả về mẫu Thymeleaf
	}











}
