package com.poly.rescontroller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.HotelDTO;
import com.poly.entity.Hotel;
import com.poly.repository.HotelRepository;

@RestController
@RequestMapping("/api")
public class HotelRestController {

    @Autowired
    private HotelRepository hotelRepository;
    @GetMapping("/hotels")
    public List<HotelDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
            .map(hotel -> new HotelDTO(hotel.getId(), hotel.getChinhanh() ,hotel.getDiachi()))
            .collect(Collectors.toList());
    }
}