package com.poly.controller;

import com.poly.entity.Room;
import com.poly.entity.User;
import com.poly.repository.RoomRepository;
import com.poly.repository.UserRepo;
import com.poly.util._enum.RoomStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private RoomRepository roomRepo;
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("")
  public String index(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("users", userRepo.findAll());
    return "admin/index";
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("revenue")
  public String revenue() {
  	return "revenue";
  }
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("confirmroom")
  public String confirmroom(Model model) {
	  model.addAttribute("rooms", roomRepo.findByStatus(RoomStatus.FALSE));
     
  	return "confirmroom";
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("/{id}")
  public String detail(@PathVariable int id, Model model) {
      Room room = roomRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + id));
      model.addAttribute("room", room);
      return "roomdetail";
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/confirm")
  public String confirmRoom(@RequestParam("id") int roomId) {
      Room room = roomRepo.findById(roomId).orElse(null);
      if (room != null) {
          room.setStatus(RoomStatus.TRUE); // Update status to TRUE
          roomRepo.save(room); // Save changes to the database
      }
      return "redirect:/room"; // Redirect back to the room list
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @PostMapping("/cancel")
  public String cancelRoom(@RequestParam("id") int roomId) {
      Room room = roomRepo.findById(roomId).orElse(null);
      if (room != null) {
          room.setStatus(RoomStatus.CANCEL); // Update status to TRUE
          roomRepo.save(room); // Save changes to the database
      }
      return "redirect:/admin/confirmroom"; // Redirect back to the room list
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("/add/roomtype")
  public String roomtype(Model model) {
   
    return "addroomtype";
  }
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("/by/roomtype")
  public String roomtypebyservice(Model model) {
   
    return "roomtypebyservice";
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("/dashboard")
  public String dashboard(Model model) {
   
    return "dashboard";
  }
}
