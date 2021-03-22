package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.exception.NoAuthorizationException;
import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.service.ArticleService;
import com.example.bloggerdemo.viewmodel.ArticleFeedVm;
import com.example.bloggerdemo.viewmodel.ArticleVm;
import com.example.bloggerdemo.viewmodel.util.BloggerResponseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    public ResponseEntity<?> getAll(){
        List<Article> articles = this.articleService
                .findAllByUser(getUserIdFromContext());
        return BloggerResponseEntity.ok(new ArticleFeedVm(articles));
    }

    @GetMapping("global-feed")
    public ResponseEntity<?> getGlobalFeed(@AuthenticationPrincipal String userId){
        List<Article> articles = this.articleService
                .getGlobalFeed();
        return BloggerResponseEntity.ok(new ArticleFeedVm(articles));
    }

    @PostMapping()
    public ResponseEntity<?> addArticle(@Valid @RequestBody ArticleParam articleParam){
        Article article = new Article();
        article.setTitle(articleParam.getTitle());
        article.setContent(articleParam.getContent());
        Article result = this.articleService
                .save(article, getUserIdFromContext());
        return BloggerResponseEntity.ok(new ArticleVm(result));
    }

    @PostMapping("react/{id}")
    public ResponseEntity<?> addReaction(@PathVariable int id, @AuthenticationPrincipal String userId){
        this.articleService.addOrRemoveUserReaction(id, Integer.parseInt(userId));
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

@Getter @Setter
class ArticleParam{
    @NotNull
    private String title;
    @NotNull
    private String content;
}