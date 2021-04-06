package com.example.bloggerdemo.integrationtest;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.UserReaction;
import com.google.gson.Gson;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
public class Experiment {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testCase1() {
        String randomTitle = UUID.randomUUID().toString();
        String randomContent = UUID.randomUUID().toString();
        Article article = new Article();
        article.setTitle(randomTitle);
        article.setContent(randomContent);
        entityManager.persist(article);

        Query query = entityManager.createQuery("SELECT a from Article a where a.title = :title")
                                    .setParameter("title",randomTitle);
        List titles = query.getResultList();

        Gson gson = new Gson();
        String json = gson.toJson(titles);
        System.out.println(json);

    }

    @Test
    @Transactional
    public void testCase2() {
        UserReaction userReaction = new UserReaction();
        userReaction.setMark("like1");
        Article article = new Article();
        article.setTitle("title173");
        article.setContent("content173");
        entityManager.persist(article);
    }
}
