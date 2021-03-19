package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.exception.NoAuthorizationException;
import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.service.ArticleService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ArticleVm>> getAll(){
        List<Article> articles = this.articleService
                .findAllByUser(getUserIdFromContext());
        List<ArticleVm> articleVms = articles.stream()
                .map(ArticleVm::new).collect(Collectors.toList());
        return ResponseEntity.ok(articleVms);
    }

    @GetMapping("global-feed")
    public ResponseEntity<List<ArticleVm>> getGlobalFeed(){
        List<Article> articles = this.articleService
                .getGlobalFeed();
        List<ArticleVm> articleVms = articles.stream()
                .map(ArticleVm::new).collect(Collectors.toList());
        return ResponseEntity.ok(articleVms);
    }

    @PostMapping()
    public ResponseEntity<ArticleVm> addArticle(@Valid @RequestBody ArticleParam articleParam){
        Article article = new Article();
        article.setTitle(articleParam.getTitle());
        article.setContent(articleParam.getContent());
        Article result = this.articleService
                .save(article, getUserIdFromContext());
        return new ResponseEntity<>(new ArticleVm(result), HttpStatus.OK);
    }

    @PostMapping("react/{id}")
    public ResponseEntity<?> addReaction(@PathVariable int id){
        this.articleService.addUserReaction(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<ArticleVm> editArticle(
            @Valid @RequestBody ArticleParam articleParam
            , @PathVariable int id){
        if (isAccessDenied(id))
            throw new NoAuthorizationException();
        Article article = new Article();
        article.setId(id);
        article.setTitle(articleParam.getTitle());
        article.setContent(articleParam.getContent());
        Article result = this.articleService.update(article);
        return new ResponseEntity<>(new ArticleVm(result), HttpStatus.OK);
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

@Getter
@Setter
class ArticleVm {
    private Integer id;
    private String title;
    private String content;
    private String author;
    private int react;

    public ArticleVm(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor().getUsername();
        this.react = article.getUserReactions().size();
    }
}

@Getter @Setter
class ArticleParam{
    @NotNull
    private String title;
    @NotNull
    private String content;
}