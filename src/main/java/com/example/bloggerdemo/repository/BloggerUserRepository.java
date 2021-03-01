package com.example.bloggerdemo.repository;

import com.example.bloggerdemo.model.BloggerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BloggerUserRepository extends JpaRepository<BloggerUser, Integer> {
    Optional<BloggerUser> findOneByUsername(String username);
}
