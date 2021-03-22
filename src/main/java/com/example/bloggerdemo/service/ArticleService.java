package com.example.bloggerdemo.service;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    public Article save(Article article, int userId){
        article.setAuthor(getBloggerUser(userId));
        return articleRepository.save(article);
    }

    public List<Article> findAllByUser(int userId) {
        return articleRepository.findByAuthorOrderByCreateAtDesc(getBloggerUser(userId));
    }

    private BloggerUser getBloggerUser(int userId){
        return bloggerUserRepository
                .findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

    }

    public Article update(Article article) {
        Article current = articleRepository.getOne(article.getId());
        current.update(
                article.getTitle(),
                article.getContent());
        return articleRepository.save(current);
    }

    public void deleteById(Integer id) {
        this.articleRepository.deleteById(id);
    }

    public List<Article> getGlobalFeed() {
        return articleRepository.findAllByOrderByCreateAtDesc();
    }

    @Transactional
    public void addOrRemoveUserReaction(int articleId, int userId){
        boolean isUserReacted = this.articleRepository.isUserReacted(articleId,userId);
        if (isUserReacted){
            this.articleRepository.addUserReactionByArticleId(articleId, userId);
        }else{
            this.articleRepository.removeUserReaction(articleId, userId);
        }
    }
}

