package com.example.bloggerdemo.repository;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
    List<Article> findByAuthor(BloggerUser author);
}
