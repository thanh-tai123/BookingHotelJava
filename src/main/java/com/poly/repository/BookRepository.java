package com.poly.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	  Book findByBookCode(String bookCode);
}
