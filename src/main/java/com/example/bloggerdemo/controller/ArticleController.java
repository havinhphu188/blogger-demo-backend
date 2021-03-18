package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.dto.ArticleDto;
import com.example.bloggerdemo.exception.NoAuthorizationException;
import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ArticleDto>> getAll(){
        List<ArticleDto> articles = this.articleService
                .findAllByUser(getUserIdFromContext());
        return ResponseEntity.ok(articles);
    }

    @GetMapping("global-feed")
    public ResponseEntity<List<ArticleDto>> getGlobalFeed(){
        List<ArticleDto> articles = this.articleService
                .getGlobalFeed();
        return ResponseEntity.ok(articles);
    }

    @PostMapping()
    public ResponseEntity<ArticleDto> addArticle(@Valid @RequestBody ArticleDto article){
        ArticleDto result = this.articleService
                .save(article, getUserIdFromContext());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("react/{id}")
    public ResponseEntity<?> addReaction(@PathVariable int id){
        this.articleService.addUserReaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleDto> editArticle(@Valid @RequestBody ArticleDto article, @PathVariable int id){
        if (isAccessDenied(id))
            throw new NoAuthorizationException();
        article.setId(id);
        ArticleDto result = this.articleService.update(article);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Integer id){
        if (isAccessDenied(id))
            throw new NoAuthorizationException();
        this.articleService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private int getUserIdFromContext(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Integer.parseInt((String) securityContext.getAuthentication().getPrincipal());
    }

    private boolean isAccessDenied(int id){
        Article article = articleRepository.getOne(id);
        return article.getAuthor().getId() != getUserIdFromContext();
    }

}
