package com.example.mysns.repository;

import com.example.mysns.entity.Comment;
import com.example.mysns.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPost(Pageable pageable, Post post);

}
