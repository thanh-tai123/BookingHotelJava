package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
	private Integer id;
    private String chinhanh;
//    private String diachi;
    private String mota;
}
