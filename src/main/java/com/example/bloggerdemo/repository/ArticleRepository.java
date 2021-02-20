package com.example.bloggerdemo.repository;

import com.example.bloggerdemo.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
