package com.poly.repository;

import java.util.List;
import java.util.Optional;

import com.poly.entity.BookDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
	  Book findByBookCode(String bookCode);


}
