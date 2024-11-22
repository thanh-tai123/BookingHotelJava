package com.poly.serviceRepository;

import java.util.List;

import com.poly.entity.Hotel;

public interface HotelServiceRepository {
	public List<Hotel> getAllHotels();
	public Hotel getHotelById(int id);
	public Hotel saveHotel(Hotel hotel);
	public Hotel updateHotel(Hotel hotel);
	public List<Hotel> getAllBranches();
}
