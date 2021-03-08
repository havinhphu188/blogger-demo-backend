package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/article")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @GetMapping("all")
    public ResponseEntity<List<Article>> getAll(){
        List<Article> articles = this.articleService
                .findAll(getUserIdFromContext());
        return ResponseEntity.ok(articles);
    }

    @PostMapping()
    public ResponseEntity<Article> addArticle(@Valid @RequestBody Article article){

        Article result = this.articleService
                .save(article, getUserIdFromContext());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Article> editArticle(@Valid @RequestBody Article article, @PathVariable int id){
        if (!isAccessible(id))
            throw new BadCredentialsException("Does not allowed");
        article.setId(id);
        Article result = this.articleService.update(article);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Integer id){
        if (!isAccessible(id))
            throw new BadCredentialsException("Does not allowed");
        this.articleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private int getUserIdFromContext(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Integer.parseInt((String) securityContext.getAuthentication().getPrincipal());
    }

    private boolean isAccessible(int id){
        Article article = articleRepository.getOne(id);
        return article.getAuthor().getId() == getUserIdFromContext();
    }

}
