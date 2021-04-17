package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j2
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, BloggerUserRepository bloggerUserRepository) {
        this.articleRepository = articleRepository;
        this.bloggerUserRepository = bloggerUserRepository;
    }

    public Article save(Article article, int userId){
        log.debug("Saving article: {}. with UserId: {}", article, userId);
        article.setAuthor(getBloggerUser(userId));
        return articleRepository.save(article);
    }

    public List<Article> findAllByUser(int userId) {
        log.debug("Find all article belong to user with id: userId");
        return articleRepository.findByAuthorOrderByCreateAtDesc(getBloggerUser(userId));
    }

    private BloggerUser getBloggerUser(int userId){
        log.debug("Getting user with id: {}", userId);
        return bloggerUserRepository
                .findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

    }

    public Article update(Article article) {
        log.debug("Updating article. Article: {}", article);
        Article current = articleRepository.getOne(article.getId());
        current.update(
                article.getTitle(),
                article.getContent());
        return articleRepository.save(current);
    }

    public void deleteById(Integer id) {
        log.debug("Deleting article by id: {}", id);
        this.articleRepository.deleteById(id);
    }

    public List<Article> getGlobalFeed() {
        log.debug("Getting global feed");
        return articleRepository.findAllByOrderByCreateAtDesc();
    }

    @Transactional
    public boolean addOrRemoveUserReaction(int articleId, int userId){
        log.debug("addOrRemoveUserReaction. Param: articleId: {}. userId: {}", articleId, userId);
        boolean isUserReacted = this.articleRepository.isUserReacted(articleId,userId);
        if (isUserReacted){
            this.articleRepository.removeUserReaction(articleId, userId);
            log.debug("Removed user reaction. articleId: {}, userId: {}", articleId, userId);
        }else{
            this.articleRepository.addUserReactionByArticleId(articleId, userId);
            log.debug("Added user reaction. articleId: {}, userId: {}", articleId, userId);
        }
        return !isUserReacted;
    }

    public Map<Integer, Boolean> checkIfCurrentUserReactToArticle(int userId, List<Article> articles) {
        log.debug("Checking If Current User React To Article");
        Map<Integer, Boolean> result = new HashMap<>();
        articles.forEach(article -> {
            boolean isUserReacted = this.articleRepository.isUserReacted(article.getId(), userId);
            result.put(article.getId(), isUserReacted);
        });
        return result;
    }

    public List<Article> getSubscriptionFeedByUser(int userId) {
        log.debug("Getting Subscription Feed By User. userId: {}", userId);
        return articleRepository.getArticleBySubscription(userId);
    }

    public List<Article> getAuthorFeed(int authorId) {
        log.debug("Getting author feed. authorId: {}", authorId);
        return articleRepository.findAllByAuthorIdOrderByCreateAtDesc(authorId);
    }

    public int getNumberOfReaction(int articleId) {
        log.debug("Getting number of reaction. articleId: {}", articleId);
        return articleRepository.getNumberOfReaction(articleId);
    }
}

