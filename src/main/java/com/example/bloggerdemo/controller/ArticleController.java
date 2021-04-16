package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.exception.NoAuthorizationException;
import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.service.business.ArticleService;
import com.example.bloggerdemo.viewmodel.ArticleFeedNoReactVm;
import com.example.bloggerdemo.viewmodel.ArticleFeedVm;
import com.example.bloggerdemo.viewmodel.ArticleVm;
import com.example.bloggerdemo.viewmodel.util.BloggerResponseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/article")
@Log4j2
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAll(@AuthenticationPrincipal String userIdString){
        log.debug("REST request to get all article, for user with id: {}", userIdString);
        List<Article> articles = this.articleService
                .findAllByUser(Integer.parseInt(userIdString));
        return BloggerResponseEntity.ok(new ArticleFeedNoReactVm(articles));
    }

    @GetMapping("global-feed")
    public ResponseEntity<Object> getGlobalFeed(@AuthenticationPrincipal String userIdString){
        log.debug("REST request to get global feed for user with id: {}", userIdString);
        List<Article> articles = this.articleService
                .getGlobalFeed();
        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException exception) {
            return BloggerResponseEntity.ok(new ArticleFeedNoReactVm(articles));
        }

        Map<Integer, Boolean> isReactedMap =
                this.articleService
                        .checkIfCurrentUserReactToArticle
                                (userId,articles);
        return BloggerResponseEntity.ok(new ArticleFeedVm(articles, isReactedMap));
    }

    @PostMapping()
    public ResponseEntity<Object> addArticle(@AuthenticationPrincipal String userIdString,
                                             @Valid @RequestBody ArticleParam articleParam){
        log.debug("REST request to add article: {}, for user with id: {}", articleParam, userIdString);
        Article article = new Article();
        article.setTitle(articleParam.getTitle());
        article.setContent(articleParam.getContent());
        Article result = this.articleService
                .save(article, Integer.parseInt(userIdString));
        return BloggerResponseEntity.ok(new ArticleVm(result));
    }

    @PostMapping("react/{articleId}")
    public ResponseEntity<Object> addOrRemoveReaction(@PathVariable int articleId, @AuthenticationPrincipal String userId){
        log.debug("REST request to add or remove Reaction on article,  articleId: {}, userId: {}", articleId, userId);
        boolean isUserReacted = this.articleService.addOrRemoveUserReaction(articleId, Integer.parseInt(userId));
        int numberOfReaction = this.articleService.getNumberOfReaction(articleId);
        Map<String,Object> response =new HashMap<>();
        response.put("isReacted",isUserReacted);
        response.put("reaction", numberOfReaction);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{articleId}")
    public ResponseEntity<ArticleVm> editArticle(
            @Valid @RequestBody ArticleParam articleParam,
            @PathVariable int articleId,
            @AuthenticationPrincipal String userIdString){
        log.debug("REST request to edit article with id: {}" +
                ", with articleParam: {}, with user with id: {}",
                articleId, articleParam, userIdString);
        if (isAccessDenied(articleId, Integer.parseInt(userIdString)))
            throw new NoAuthorizationException();
        Article article = new Article();
        article.setId(articleId);
        article.setTitle(articleParam.getTitle());
        article.setContent(articleParam.getContent());
        Article result = this.articleService.update(article);
        return new ResponseEntity<>(new ArticleVm(result), HttpStatus.OK);
    }

    @DeleteMapping("{articleId}")
    public ResponseEntity<Object> deleteArticle(@PathVariable Integer articleId, @AuthenticationPrincipal String userIdString){
        log.debug("REST request to delete article. articleId: {}, userId: {}", articleId,userIdString);
        if (isAccessDenied(articleId, Integer.parseInt(userIdString)))
            throw new NoAuthorizationException();
        this.articleService.deleteById(articleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("subscriptions-feed")
    public ResponseEntity<Object> getSubscriptionFeed(@AuthenticationPrincipal String userIdString){
        log.debug("REST request to get subscriptions feed. userId: {}", userIdString);
        int userId = Integer.parseInt(userIdString);
        List<Article> articles = this.articleService
                .getSubscriptionFeedByUser(userId);

        Map<Integer, Boolean> isReactedMap =
                this.articleService
                        .checkIfCurrentUserReactToArticle
                                (userId,articles);
        return BloggerResponseEntity.ok(new ArticleFeedVm(articles, isReactedMap));
    }

    @GetMapping("author-feed/{authorId}")
    public ResponseEntity<Object> getAuthorFeed(@AuthenticationPrincipal String userId,
                                                @PathVariable int authorId){
        log.debug("REST request to get author feed. userId: {}, authorId: {}", userId, authorId);
        List<Article> articles = this.articleService
                .getAuthorFeed(authorId);

        Map<Integer, Boolean> isReactedMap =
                this.articleService
                        .checkIfCurrentUserReactToArticle
                                (Integer.parseInt(userId),articles);
        return BloggerResponseEntity.ok(new ArticleFeedVm(articles, isReactedMap));
    }

    private boolean isAccessDenied(int articleId, int userId){
        Article article = articleRepository.getOne(articleId);
        return article.getAuthor().getId() != userId;
    }

}

@Getter @Setter
class ArticleParam{
    @NotNull
    private String title;
    @NotNull
    private String content;
}