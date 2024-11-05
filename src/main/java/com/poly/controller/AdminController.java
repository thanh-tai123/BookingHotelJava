package com.poly.controller;

import com.poly.entity.Role;
import com.poly.entity.Room;
import com.poly.entity.RoomType;
import com.poly.entity.Services;
import com.poly.entity.User;
import com.poly.repository.RoomRepository;
import com.poly.repository.RoomTypeByServiceRepository;
import com.poly.repository.RoomTypeRepository;
import com.poly.repository.ServiceRepository;
import com.poly.repository.UserRepo;
import com.poly.service.RoleService;
import com.poly.service.RoomTypeService;
import com.poly.service.ServiceService;
import com.poly.util._enum.RoomStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoomRepository roomRepo;
    @Autowired
    private ServiceRepository serviceRepo;
    @Autowired
    private RoleService roleService;
    @Autowired 
    private RoomTypeRepository roomTypeRepo;
    @Autowired
    private RoomTypeByServiceRepository roomtypebyServiceRepo;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    

   

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
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("/add/service")
  public String roomservice(Model model) {
   
    return "addservice";
  }
  
  
  
  ////////
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("")
  public String index(Model model) {
      model.addAttribute("user", new User());
      model.addAttribute("users", userRepo.findAll());
      return "admin/index";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("/edit/{id}")
  public String showEditRoleUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
      User user = this.userRepo.findById(id).get();

      List<Role> roles = this.roleService.findAll();

      model.addAttribute("user", user);
      model.addAttribute("roles", roles);

      return "admin/Edit_User";
  }
  @PostMapping("/save")
  public String saveUser(@ModelAttribute(name = "user") User user, RedirectAttributes redirectAttributes
  ) {
      this.userRepo.save(user);
      return "redirect:/admin";
  }
  //////
  
  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("roomtypes")
  public String indexroomtype(Model model) {
      model.addAttribute("roomType", new RoomType());
      model.addAttribute("roomTypes", roomTypeRepo.findAll());
      return "roomtypeandservice/index";
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @RequestMapping("/editRoomType/{id}")
  public String showEditRoomType(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
      RoomType roomType = roomTypeRepo.findById(id).orElseThrow(() -> new RuntimeException("RoomType not found"));
      List<Services> services = serviceRepo.findAll();
      Set<Integer> selectedServices = roomType.getServices().stream()
                                              .map(rts -> rts.getMyService().getId())
                                              .collect(Collectors.toSet());
      
      
      model.addAttribute("roomType", roomType);
      model.addAttribute("services", services);
      model.addAttribute("selectedServices", selectedServices);

      return "roomtypeandservice/Edit_RoomType";
  }


  @PostMapping("/saveRoomType")
  public String saveRoomType(@ModelAttribute(name = "roomType") RoomType roomType, @RequestParam(name = "serviceIds", required = false) List<Integer> serviceIds) {
      roomTypeService.updateRoomTypeServices(roomType, serviceIds);
      return "redirect:/admin/roomtypes";
  }

}
