package com.poly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.Comment;
import com.poly.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

    public void respondToComment(int commentId, String adminResponse) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setAdminResponse(adminResponse);
        commentRepository.save(comment);
    }
}
