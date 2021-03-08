package com.example.bloggerdemo.controller;

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

    private final ArticleRepository articleRepository;
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, ArticleService articleService) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
    }

    @GetMapping("all")
    public ResponseEntity<List<Article>> getAll(){
        List<Article> articles = this.articleRepository.findAll();
        return ResponseEntity.ok(articles);
    }

    @PostMapping()
    public ResponseEntity<Article> addArticle(@Valid @RequestBody Article article){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String username = (String) securityContext.getAuthentication().getPrincipal();
        Article result = this.articleService.save(article,username);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Article> editArticle(@Valid @RequestBody Article article, @PathVariable int id){
        article.setId(id);
        Article result = this.articleRepository.save(article);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Integer id){
        this.articleRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
