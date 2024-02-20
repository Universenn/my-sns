package com.example.mysns.repository;

import com.example.mysns.entity.Post;
import com.example.mysns.entity.PostLike;
import com.example.mysns.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostAndUser(Post post, User user);
}