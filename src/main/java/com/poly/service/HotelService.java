package com.poly.service;

import com.poly.entity.Hotel;
import com.poly.repository.HotelRepository;
import com.poly.serviceRepository.HotelServiceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService implements HotelServiceRepository{
    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        return this.hotelRepository.findAll();
    }

    public Hotel getHotelById(int id) {
        return this.hotelRepository.findById(id).get();
    }

    public Hotel saveHotel(Hotel hotel) {
        return this.hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Hotel hotel) {
        Hotel hotelOld = this.getHotelById(hotel.getId());
        hotel.setRooms(hotelOld.getRooms());
        return this.hotelRepository.save(hotel);
    }
    public List<Hotel> getAllBranches() {
        return hotelRepository.findAll();

    }


}
