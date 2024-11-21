package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeDto {
	 private String username;
	 private String oldPassword;
	    private String newPassword;
	    private String confirmNewPassword;
}