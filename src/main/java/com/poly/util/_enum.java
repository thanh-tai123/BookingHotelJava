package com.poly.util;

import lombok.Data;

@Data
public class _enum {
  public enum RoleUserEnum {
//    ADMIN_HCM(0),
//    ADMIN_HN(3)
	  ADMIN(0),
    USER(1),
    SUPERADMIN(2),
	  STAFF(3);
    private final int value;

    RoleUserEnum(int value) {
      this.value = value;
    }

    public int getValue() {
      return this.value;
    }

  }
  public enum RoomStatus {
	    FALSE, TRUE, CANCEL
	}

  public enum AuthTypeEnum {
    LOCAL,
    GOOGLE,
    FACEBOOK
  }
}