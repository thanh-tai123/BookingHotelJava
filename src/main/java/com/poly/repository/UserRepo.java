package com.poly.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  User findByName(String name);
  // Phương thức phân trang
  Page<User> findAll(Pageable pageable);

  // Tìm kiếm user theo email (không phân biệt chữ hoa, chữ thường)
  List<User> findByEmailContainingIgnoreCase(String email);
  Optional<User> findByResetToken(String token);
}
