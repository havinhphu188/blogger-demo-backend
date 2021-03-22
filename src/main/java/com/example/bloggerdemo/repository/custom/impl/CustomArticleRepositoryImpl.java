package com.example.bloggerdemo.repository.custom.impl;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.UserReaction;
import com.example.bloggerdemo.repository.custom.CustomArticleRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class CustomArticleRepositoryImpl implements CustomArticleRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void addUserReactionByArticleId(Integer articleId, Integer userId) {
        UserReaction userReaction = new UserReaction();
        Article article = entityManager.getReference(Article.class, articleId);
        BloggerUser bloggerUser = entityManager.getReference(BloggerUser.class, userId);
        userReaction.setArticle(article);
        userReaction.setBloggerUser(bloggerUser);
        entityManager.persist(userReaction);
    }

    @Override
    public boolean isUserReacted(Integer articleId, Integer userId){
        List result = entityManager.createQuery("select r from UserReaction as r " +
                "where r.article.id = :articleId and r.bloggerUser.id = :userId")
                .setParameter("articleId",articleId)
                .setParameter("userId", userId)
                .getResultList();
        return result.isEmpty();
    }

    @Override
    public void removeUserReaction(Integer articleId, Integer userId) {
        entityManager.createQuery("delete from UserReaction r where r.article.id =:articleId and r.bloggerUser.id =:userId")
                .setParameter("articleId", articleId)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
