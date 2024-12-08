package com.poly.controller;

import com.poly.entity.Hotel;
import com.poly.entity.Role;
import com.poly.entity.Room;
import com.poly.entity.RoomImages;
import com.poly.entity.RoomType;
import com.poly.entity.RoomTypeByService;
import com.poly.entity.Services;
import com.poly.entity.User;
import com.poly.repository.RoomRepository;
import com.poly.repository.RoomTypeByServiceRepository;
import com.poly.repository.RoomTypeRepository;
import com.poly.repository.ServiceRepository;
import com.poly.repository.UserRepo;
import com.poly.service.HotelService;
import com.poly.service.RoleService;
import com.poly.service.RoomTypeService;
import com.poly.service.ServiceService;
import com.poly.service.UserService;
import com.poly.util._enum.AuthTypeEnum;
import com.poly.util._enum.RoomStatus;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
@Lazy
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
    private UserService userService;
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
    @Autowired
    private HotelService hotelService;


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("revenue")
    public String revenue() {
        return "dashboard/revenue";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("confirmroom")
    public String confirmroom(Model model) {


        model.addAttribute("rooms", roomRepo.findByStatus(RoomStatus.FALSE));

        return "admin/confirmroom";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/{id}")
    public String detail(@PathVariable int id, Model model) {
        Room room = roomRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + id));

        List<RoomTypeByService> services = room.getRoomtype().getServices();

        List<RoomImages> roomImg = room.getRoomImages();

        model.addAttribute("room", room);
        model.addAttribute("services", services);
        model.addAttribute("roomImgs", roomImg);
        return "admin/roomdetail";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/confirm")
    public String confirmRoom(@RequestParam("id") int roomId, @RequestParam("note") String note) {


        Room room = roomRepo.findById(roomId).orElse(null);
        if (room != null) {
            room.setStatus(RoomStatus.TRUE); // Update status to TRUE
            room.setNote(note); // Save note
            roomRepo.save(room); // Save changes to the database
        }
        return "redirect:/room"; // Redirect back to the room list
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/cancel")
    public String cancelRoom(@RequestParam("id") int roomId, @RequestParam("note") String note) {
        Room room = roomRepo.findById(roomId).orElse(null);
        if (room != null) {
            room.setStatus(RoomStatus.CANCEL); // Update status to CANCEL
            room.setNote(note); // Save note
            roomRepo.save(room); // Save changes to the database
        }
        return "redirect:/admin/confirmroom"; // Redirect back to the room list
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/add/roomtype")
    public String roomtype(Model model) {

        return "admin/addroomtype";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/dashboard")
    public String dashboard(Model model) {

        return "dashboard";
    }


//    @PreAuthorize("hasAuthority('ADMIN')")
//    @GetMapping("")
//    public String getUsers(Model model) {
//        // Giả sử bạn có danh sách người dùng từ service
//        List<User> users = userService.getAllUsers();
//
//        // Tính tổng số người dùng
//        int totalUsers = users.size();
//
//        // Tính số tài khoản Google và Local
//        long googleAccounts = users.stream().filter(u -> u.getAuthType() == AuthTypeEnum.GOOGLE).count();
//        long localAccounts = users.stream().filter(u -> u.getAuthType() == AuthTypeEnum.LOCAL).count();
//
//        // Tính số vai trò (tính duy nhất)
//        Set<String> roles = users.stream()
//                                 .flatMap(u -> Arrays.stream(u.getRoleString().split(", ")))
//                                 .collect(Collectors.toSet());
//
//        // Gửi dữ liệu sang giao diện
//        model.addAttribute("users", users);
//        model.addAttribute("totalUsers", totalUsers);
//        model.addAttribute("googleAccounts", googleAccounts);
//        model.addAttribute("localAccounts", localAccounts);
//        model.addAttribute("totalRoles", roles.size());
//
//        return "admin/index";
//    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("")
    public String getUsers(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        // Tạo Pageable với số trang và kích thước trang
        Pageable pageable = PageRequest.of(page, 10); // 10 là số người dùng mỗi trang

        // Lấy danh sách người dùng phân trang
        Page<User> usersPage = userRepo.findAll(pageable);

        // Thống kê số lượng người dùng
        model.addAttribute("googleAccounts", usersPage.getContent().stream().filter(u -> u.getAuthType() == AuthTypeEnum.GOOGLE).count());
        model.addAttribute("localAccounts", usersPage.getContent().stream().filter(u -> u.getAuthType() == AuthTypeEnum.LOCAL).count());

        // Tính tổng số vai trò
        Set<String> roles = usersPage.getContent().stream()
                .flatMap(u -> Arrays.stream(u.getRoleString().split(", ")))
                .collect(Collectors.toSet());
        model.addAttribute("totalRoles", roles.size());

        model.addAttribute("txtemail", "");

        // Thêm thông tin phân trang vào model
        model.addAttribute("users", usersPage.getContent());
        model.addAttribute("totalUsers", usersPage.getTotalElements());
        model.addAttribute("totalPages", usersPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "admin/index";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user-search-email")
    public String searchUserByEmail(@RequestParam("email") String email, Model model) {
        // Tìm kiếm user theo email
        List<User> users = userRepo.findByEmailContainingIgnoreCase(email);

        // Kiểm tra danh sách user
        if (users.isEmpty()) {
            model.addAttribute("message", "Không tìm thấy người dùng với email: " + email);
        }
        model.addAttribute("txtemail", email);

        // Đặt giá trị phân trang tạm thời (vì tìm kiếm không cần phân trang)
        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("totalPages", 1); // Chỉ 1 trang kết quả
        model.addAttribute("currentPage", 0); // Trang đầu tiên

        // Thống kê số lượng loại tài khoản
        model.addAttribute("googleAccounts", users.stream().filter(u -> u.getAuthType() == AuthTypeEnum.GOOGLE).count());
        model.addAttribute("localAccounts", users.stream().filter(u -> u.getAuthType() == AuthTypeEnum.LOCAL).count());

        // Tính tổng số vai trò
        Set<String> roles = users.stream()
                .flatMap(u -> Arrays.stream(u.getRoleString().split(", ")))
                .collect(Collectors.toSet());
        model.addAttribute("totalRoles", roles.size());

        return "admin/index";
    }



    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/edit/{id}")
    public String showEditRoleUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirectAttributes) {
        User user = this.userRepo.findById(id).get();

        List<Role> roles = this.roleService.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "admin/Edit_User";
    }
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @PostMapping("/save")
    public String saveUser(@ModelAttribute(name = "user") User user, RedirectAttributes redirectAttributes
    ) {
        this.userRepo.save(user);
        return "redirect:/admin";
    }


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


    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/add/service")
    public String roomservice(Model model) {

        return "admin/addservice";
    }

    //Them chi nhanh
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/show-chinhanh")
    public String showChinhanh(Model model) {
        List<Hotel> hotels = this.hotelService.getAllHotels();
        model.addAttribute("hotels", hotels);
        return "admin/chi_nhanh_hotel";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/show-add-chinhanh")
    public String showAddChinhanh() {
        return "admin/add_ChiNhanh";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/save-chinhanh")
    public String saveChiNhanh(@ModelAttribute Hotel hotel, Model model) {
        this.hotelService.saveHotel(hotel);
        return "redirect:/admin/show-chinhanh";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/show-edit-chinhanh/{id}")
    public String showEditChiNhanh(@PathVariable(name = "id") Integer id,
                                   Model model) {
        Hotel hotel = this.hotelService.getHotelById(id);
        model.addAttribute("hotel", hotel);
        return "admin/Edit_ChiNhanh_Hotel";
    }

    @PreAuthorize("hasAuthority('SUPERADMIN')")
    @RequestMapping("/update-chinhanh")
    public String updateChiNhanh(@ModelAttribute Hotel hotel, Model model) {
        this.hotelService.updateHotel(hotel);
        return "redirect:/admin/show-chinhanh";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/menudoc")
    public String menu() {
        return "layout/menudoc";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/userbybook")
    public String userbybook() {
        return "dashboard/userbybook";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/compare")
    public String compare() {
        return "dashboard/compare";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/branch")
    public String branch() {
        return "dashboard/branch";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/search/room-per-branch")
    public String RoomPerBranch() {
        return "dashboard/tableroomperbranch";
    }


}
