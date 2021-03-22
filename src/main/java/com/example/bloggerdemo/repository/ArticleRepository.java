package com.example.bloggerdemo.repository;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.custom.CustomArticleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer>, CustomArticleRepository {
    List<Article> findByAuthorOrderByCreateAtDesc(BloggerUser author);
    List<Article> findAllByOrderByCreateAtDesc();
}
