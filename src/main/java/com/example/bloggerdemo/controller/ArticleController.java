package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/article")
public class ArticleController {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("all")
    public ResponseEntity<List<Article>> getAll(){
        List<Article> articles = this.articleRepository.findAll();
        return ResponseEntity.ok(articles);
    }

    @PostMapping()
    public ResponseEntity<Article> addArticle(@Valid @RequestBody Article article){
        Article result = this.articleRepository.save(article);
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
