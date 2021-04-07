package com.example.bloggerdemo.repository;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.Subscription;
import com.example.bloggerdemo.model.UserReaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ArticleRepositoryTest {

    private final int TEST_USER_ID = 1;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void findByAuthorTest(){
        List<Article> articles = articleRepository.findAllByOrderByCreateAtDesc();
        assertFalse(articles.isEmpty());
        assertTrue(isOrderedByCreatedDateDesc(articles));
    }

    @Test
    void findByAuthorOrderByCreateAtDescTest(){
        BloggerUser user = entityManager.getReference(BloggerUser.class, TEST_USER_ID);
        List<Article> articles = articleRepository.findByAuthorOrderByCreateAtDesc(user);
        assertFalse(articles.isEmpty());
        assertTrue(isOrderedByCreatedDateDesc(articles));
    }

    @Test
    void findAllByAuthorIdTest(){
        List<Article> articles = articleRepository.findAllByAuthorId(TEST_USER_ID);
        assertFalse(articles.isEmpty());
    }

    @Test
    @Transactional
    void getArticleBySubscriptionTest(){
        BloggerUser author = entityManager.getReference(BloggerUser.class, 2);
        Article article = entityManager.find(Article.class, 1);
        article.setAuthor(author);
        entityManager.persist(article);

        BloggerUser user = entityManager.getReference(BloggerUser.class, TEST_USER_ID);
        Subscription subscription = new Subscription();
        subscription.setFollower(user);
        subscription.setFollowee(author);
        entityManager.persist(subscription);

        List<Article> articles = articleRepository.getArticleBySubscription(TEST_USER_ID);
        assertFalse(articles.isEmpty());
    }

    @Test
    @Transactional
    void isUserReactedTest(){
        entityManager.createQuery("delete from UserReaction u " +
                "where u.bloggerUser.id =:userId and u.article.id =:articleId")
                .setParameter("userId", 1)
                .setParameter("articleId",1).executeUpdate();

        assertFalse(articleRepository.isUserReacted(1, 1));

        UserReaction userReaction = new UserReaction();
        userReaction.setArticle(entityManager.getReference(Article.class, 1));
        userReaction.setBloggerUser(entityManager.getReference(BloggerUser.class, 1));
        entityManager.persist(userReaction);

        assertTrue(articleRepository.isUserReacted(1, 1));
    }

    @Transactional
    @Test
    void removeUserReactionTest(){
        Query countReactionQuery = entityManager.createQuery("select count(r) from UserReaction r where r.bloggerUser.id =:userId and r.article.id =:articleId")
                .setParameter("userId",1)
                .setParameter("articleId",1);
        long countReactionBefore = (long) countReactionQuery.getSingleResult();
        assertEquals(1, countReactionBefore);

        articleRepository.removeUserReaction(1,1);

        long countReactionAfter = (long) countReactionQuery.getSingleResult();
        assertEquals(0, countReactionAfter);
    }

    @Test
    @Transactional
    void addUserReactionByArticleIdTest(){
        final int userId = 2; final int articleId = 4;
        Query countReactionQuery = entityManager.createQuery("select count(r) from UserReaction r where r.bloggerUser.id =:userId and r.article.id =:articleId")
                .setParameter("userId",userId)
                .setParameter("articleId",articleId);

        long countReactionBefore = (long) countReactionQuery.getSingleResult();
        assertEquals(0, countReactionBefore);
        articleRepository.addUserReactionByArticleId(articleId,userId);

        long countReactionAfter = (long) countReactionQuery.getSingleResult();
        assertEquals(1, countReactionAfter);
    }

    @Test
    @Transactional
    void getNumberOfReactionTest(){
        final int articleId = 1;
        Query countReactionQuery = entityManager.createQuery("select count(r) from UserReaction r where r.article.id =:articleId")
                .setParameter("articleId",articleId);
        long countQuery = (long) countReactionQuery.getSingleResult();
        assertEquals((int)countQuery, articleRepository.getNumberOfReaction(articleId));
    }

    private boolean isOrderedByCreatedDateDesc(List<Article> articles) {
        for (int i = 1; i < articles.size(); i++) {
            if (articles.get(i).getCreateAt()
                    .isAfter(articles.get(i-1).getCreateAt())){
                return false;
            }
        }
        return true;
    }

}