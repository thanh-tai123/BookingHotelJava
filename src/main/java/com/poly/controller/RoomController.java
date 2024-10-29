package com.poly.controller;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
import com.poly.util._enum.RoomStatus;

@Controller
@RequestMapping("/room")
public class RoomController {
	 @Autowired
	  private RoomRepository roomRepo;
	 @RequestMapping("")
	    public String index(Model model) {
	        // Fetch only rooms with status TRUE
	        model.addAttribute("rooms", roomRepo.findByStatus(RoomStatus.TRUE));
	        return "room";
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
	        Room room = roomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + id));
	        List<RoomTypeByService> services = room.getRoomtype().getServices();
	        
	        model.addAttribute("room", room);
	        model.addAttribute("services", services);
	        return "roomdetailversion";
	    }
		/*
		 * @GetMapping(value = {"/book-room/**"}) public String index() { return "book";
		 * // This should point to your AngularJS app's main HTML file }
		 */
	  
	  @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
	    @GetMapping("/room-details/{roomId}")
	    public String showRoomDetails(@PathVariable Integer roomId, Model model) {
	        model.addAttribute("roomId", roomId); // Pass roomId to Thymeleaf view
	        return "book"; // This is the Thymeleaf template to show room details
	    }
	    @RequestMapping("displaypayment")
	    public String display(Model model) {
	        // Fetch only rooms with status TRUE
	       
	        return "display";
	    }
}
