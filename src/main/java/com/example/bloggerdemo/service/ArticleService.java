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
        article.setAuthor(getBloggerUser(username));
        return articleRepository.save(article);
    }

    public List<Article> findAll(String username) {
        return articleRepository.findByAuthor(getBloggerUser(username));
    }

    private BloggerUser getBloggerUser(String username){
        return bloggerUserRepository
                .findOneByUsername(username).orElseThrow(() -> new EntityNotFoundException("Entity " + username + " not found"));

    }
}

