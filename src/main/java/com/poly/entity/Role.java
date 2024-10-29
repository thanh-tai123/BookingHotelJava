package com.poly.entity;

import com.poly.util._enum.*;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@NoArgsConstructor
public class Role {
  @Id
  private Integer id;
  private String name;
  public Role(RoleUserEnum role ) {
    this.id = role.getValue();
    this.name = role.name();
  }

}
