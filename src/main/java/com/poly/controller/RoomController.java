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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.entity.Room;
import com.poly.entity.RoomImages;
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

		Pageable pageable = PageRequest.of(pageNum - 1, 3, Sort.by(Sort.Order.by(sortField).with(Sort.Direction.fromString(sortDir))));
		Page<Room> page = roomRepo.findByStatus(RoomStatus.TRUE, pageable);
		List<Room> rooms = page.getContent();

		Map<Integer, Integer> visitCounts = new HashMap<>();
		for (Room room : rooms) {
			int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
			visitCounts.put(room.getId(), visitCount);
		}

		List<RoomType> roomTypes = roomTypeService.getAllRoomType();
		List<Hotel> hotels = hotelService.getAllHotels();
		List<String> priceRanges = Arrays.asList("1000-5000", "5000-10000", "10000-20000");

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
		model.addAttribute("hasPrevious", page.hasPrevious());
		model.addAttribute("hasNext", page.hasNext());

		return "room";
	}


	@RequestMapping("/search")
	public String searchAvailableRooms(
			@RequestParam int hotelId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkin,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date checkout,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "sortField", defaultValue = "id") String sortField,
			@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
			Model model) {

		// Tạo Pageable để phân trang
		Pageable pageable = PageRequest.of(pageNum - 1, 3, Sort.by(Sort.Order.by(sortField).with(Sort.Direction.fromString(sortDir))));

		// Lấy danh sách phòng trống với phân trang
		Page<Room> page = roomRepo.findAvailableRooms(hotelId, checkin, checkout, RoomStatus.TRUE, pageable);
		List<Room> availableRooms = page.getContent();

		// Tính số lượt xem cho từng phòng
		Map<Integer, Integer> visitCounts = new HashMap<>();
		for (Room room : availableRooms) {
			int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
			visitCounts.put(room.getId(), visitCount);
		}

		// Lấy thông tin các room type và hotel
		List<Hotel> branches = hotelService.getAllHotels();
		List<RoomType> roomTypes = roomTypeService.getAllRoomType();

		// Thêm các thuộc tính vào model
		model.addAttribute("roomTypes", roomTypes);
		model.addAttribute("branches", branches);
		model.addAttribute("checkin", checkin);
		model.addAttribute("checkout", checkout);
		model.addAttribute("rooms", availableRooms);
		model.addAttribute("visitCounts", visitCounts);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("hasPrevious", page.hasPrevious());
		model.addAttribute("hasNext", page.hasNext());

		return "room";
	}



	@RequestMapping("/{id}")
	public String detail(@PathVariable int id, Model model) {
	    Integer userId = getCurrentUserId();

	    // Retrieve the room with the given ID
	    Room room = roomRepo.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + id));
	    
	    // Get services related to the room type
	    List<RoomTypeByService> services = room.getRoomtype().getServices();
	    
	    // Get the list of images for this room
	    List<RoomImages> roomImg = room.getRoomImages();
	    
	    // Record the room view (you may want to implement this in your service)
	    roomService.viewRoomDetails(id, userId);
	    
	    // Set model attributes for the view
	    model.addAttribute("room", room);
	    model.addAttribute("services", services);
	    model.addAttribute("roomImgs", roomImg);

	    return "roomdetailversion"; // The name of your Thymeleaf template
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
							@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
							@RequestParam(value = "sortField", defaultValue = "id") String sortField,
							@RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
							Model model) {

		// Kiểm tra và đảm bảo pageNum không nhỏ hơn 1
		if (pageNum < 1) {
			pageNum = 1;
		}

		// Tạo Pageable cho phân trang (bắt đầu từ pageNum - 1)
		Pageable pageable = PageRequest.of(pageNum - 1, 3, Sort.by(Sort.Order.by(sortField).with(Sort.Direction.fromString(sortDir))));

		// Tạo điều kiện lọc
		Specification<Room> specification = Specification.where(null);

		if (roomtype != null && !roomtype.isEmpty() && !roomtype.equals("Tất cả")) {
			specification = specification.and((root, query, criteriaBuilder) ->
					criteriaBuilder.equal(root.get("roomtype").get("name"), roomtype));
		}

		if (priceRange != null && !priceRange.isEmpty() && !priceRange.equals("Tất cả")) {
			String[] priceParts = priceRange.split("-");
			try {
				int minPrice = Integer.parseInt(priceParts[0].trim());
				int maxPrice = Integer.parseInt(priceParts[1].trim());

				specification = specification.and((root, query, criteriaBuilder) ->
						criteriaBuilder.between(root.get("gia"), minPrice, maxPrice));
			} catch (NumberFormatException e) {
				model.addAttribute("message", "Khoảng giá không hợp lệ.");
			}
		}

		// Áp dụng phân trang và lọc
		Page<Room> page = roomRepo.findAll(specification, pageable);
		List<Room> rooms = page.getContent();

		// Kiểm tra nếu không có phòng nào, hiển thị thông báo
		if (rooms.isEmpty()) {
			model.addAttribute("message", "Không có phòng nào phù hợp với tiêu chí tìm kiếm.");
		}

		// Tính số lượt xem cho từng phòng
		Map<Integer, Integer> visitCounts = new HashMap<>();
		for (Room room : rooms) {
			int visitCount = viewRoomRepository.getTotalVisitCountByRoomId(room.getId());
			visitCounts.put(room.getId(), visitCount);
		}

		// Lấy thông tin các room type, hotel và price ranges
		List<Hotel> branches = hotelService.getAllHotels();
		List<RoomType> roomTypes = roomTypeService.getAllRoomType();
		List<String> priceRanges = Arrays.asList("1000-5000", "5000-10000", "10000-20000");

		// Thêm thông tin vào model
		model.addAttribute("rooms", rooms);
		model.addAttribute("visitCounts", visitCounts);
		model.addAttribute("roomTypes", roomTypes);
		model.addAttribute("branches", branches);
		model.addAttribute("priceRanges", priceRanges);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("roomtype", roomtype);  // Duy trì giá trị lọc
		model.addAttribute("priceRange", priceRange); // Duy trì giá trị lọc
		model.addAttribute("hasPrevious", page.hasPrevious());
		model.addAttribute("hasNext", page.hasNext());

		return "room";
	}







	@GetMapping("/room-type-chart")
    public String getRoomTypeChart(Model model) {
        Map<String, Long> roomTypeCounts = roomService.getRoomTypeCounts();
        model.addAttribute("roomTypeCounts", roomTypeCounts);
        return "roomTypeChart";
    }









}
