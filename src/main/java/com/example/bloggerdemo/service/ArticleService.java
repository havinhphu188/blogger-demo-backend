package com.example.bloggerdemo.service;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, BloggerUserRepository bloggerUserRepository) {
        this.articleRepository = articleRepository;
        this.bloggerUserRepository = bloggerUserRepository;
    }

    public Article save(Article article, String username){
        BloggerUser bloggerUser = bloggerUserRepository
                .findOneByUsername(username).orElseThrow(() -> new EntityNotFoundException("Entity " + username + " not found"));
        article.setAuthor(bloggerUser);
        return articleRepository.save(article);
    }

    public List<Article> findAll(String username) {
        BloggerUser bloggerUser = bloggerUserRepository
                .findOneByUsername(username).orElseThrow(() -> new EntityNotFoundException("Entity " + username + " not found"));

        return articleRepository.findByAuthor(bloggerUser);
    }
}

