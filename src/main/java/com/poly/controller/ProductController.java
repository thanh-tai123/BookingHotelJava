package com.poly.controller;

import com.poly.entity.Product;
import com.poly.repository.ProductRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
	@GetMapping("/home")
	public String home() {
		return "dashboard/index";
	}
	@GetMapping("/about")
	public String about() {
		return "home/about";
	}
    @GetMapping("/demolayout")
    public String showDemo(Model model) {
        return "/layout/demolayout";  // Trả về nội dung cho mục "Hỏi đáp"
    }
  @Autowired
  private ProductRepo productRepo;
  @RequestMapping("")
  public String index(Model model) {
    model.addAttribute("products", productRepo.findAll());
    return "product";
  }
  private void fakeProduct() {
    productRepo.saveAll(List.of(
        Product.builder()
            .name("Madaz 3")
            .quantity(500)
            .price(2380000.0)
            .build(),
        Product.builder()
            .name("Iphone 15")
            .quantity(3000)
            .price(30000.0)
            .build(),
        Product.builder()
            .name("Asus tuf gaming")
            .quantity(400)
            .price(25000.0)
            .build()
    ));
  }
}
