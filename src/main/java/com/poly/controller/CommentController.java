package com.poly.controller;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.amazonaws.auth.policy.Principal;
import com.poly.entity.Comment;
import com.poly.entity.Room;
import com.poly.repository.CommentRepository;
import com.poly.repository.RoomRepository;

@Controller
@RequestMapping("/comments")
@Lazy
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RoomRepository roomRepository;
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @PostMapping("/add")
    public String addComment(@RequestParam int roomId, 
                             @RequestParam String content, 
                             @RequestParam int rating, 
                             @AuthenticationPrincipal UserDetails currentUser) {
    	
        Room room = roomRepository.findById(roomId).orElseThrow(() -> new IllegalArgumentException("Invalid room Id:" + roomId));
        Comment comment = new Comment();
        comment.setRoom(room);
        comment.setContent(content);
        comment.setRating(rating);
        comment.setUsername(currentUser.getUsername()); // Lấy tên đăng nhập từ Principal
        comment.setCreatedAt(new Date());
        commentRepository.save(comment);
        return "redirect:/room/" + roomId;
    }
}