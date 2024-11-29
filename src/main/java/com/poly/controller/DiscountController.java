package com.poly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poly.entity.Discount;
import com.poly.repository.RoomRepository;
import com.poly.service.DiscountService;
import com.poly.service.RoomService;
import com.poly.util._enum.RoomStatus;

@Controller
@RequestMapping("/discounts")
@PreAuthorize("hasAuthority('ADMIN')")
public class DiscountController {
	  @Autowired
	    private DiscountService discountService;

	    @Autowired
	    private RoomService roomService;
	    @Autowired
	    private RoomRepository roomRepository;
	    @GetMapping
	    public String listDiscounts(Model model) {
	        List<Discount> discounts = discountService.getAllDiscounts();
	        model.addAttribute("discounts", discounts);
	        return "admin/discounts";
	    }

	    @GetMapping("/new")
	    public String newDiscountForm(Model model) {
	        model.addAttribute("discount", new Discount());
	        model.addAttribute("rooms", roomRepository.findByStatus(RoomStatus.TRUE));
	        return "admin/discount-form";
	    }

	    @GetMapping("/edit/{id}")
	    public String editDiscountForm(@PathVariable int id, Model model) {
	        Discount discount = discountService.getDiscountById(id);
	        model.addAttribute("discount", discount);
	        model.addAttribute("rooms", roomRepository.findByStatus(RoomStatus.TRUE));
	        return "admin/discount-form";
	    }

	    @PostMapping("/save")
	    public String saveDiscount(@ModelAttribute Discount discount) {
	        discountService.saveDiscount(discount);
	        return "redirect:/discounts";
	    }

	    @GetMapping("/delete/{id}")
	    public String deleteDiscount(@PathVariable int id) {
	        discountService.deleteDiscount(id);
	        return "redirect:/discounts";
	    }
}
