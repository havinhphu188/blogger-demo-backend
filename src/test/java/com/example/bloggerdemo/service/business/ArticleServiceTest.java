package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @Test
    void checkIfCurrentUserReactToArticleTest(){
        final int userId = 1;
        List<Article> articles = new ArrayList<>();
        Article article1 = new Article();
        article1.setId(1);
        Article article2 = new Article();
        article2.setId(2);
        Article article3 = new Article();
        article3.setId(3);
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        when(articleRepository.isUserReacted(1, userId)).thenReturn(true);
        when(articleRepository.isUserReacted(2, userId)).thenReturn(false);
        when(articleRepository.isUserReacted(3, userId)).thenReturn(true);

        Map<Integer,Boolean> result = articleService.checkIfCurrentUserReactToArticle(userId, articles);

        assertTrue(result.get(1));
        assertFalse(result.get(2));
        assertTrue(result.get(3));
    }

    @Test
    void removeExistingUserReactionTest(){
        final int articleId = 1;
        final int userId = 1;

        when(articleRepository.isUserReacted(articleId, userId)).thenReturn(true);
        assertFalse(articleService.addOrRemoveUserReaction(articleId, userId));
        verify(articleRepository).removeUserReaction(articleId,userId);

    }

    @Test
    void addUserReactionTest(){
        final int articleId = 1;
        final int userId = 1;
        when(articleRepository.isUserReacted(articleId, userId)).thenReturn(false);
        assertTrue(articleService.addOrRemoveUserReaction(articleId, userId));
        verify(articleRepository).addUserReactionByArticleId(articleId,userId);
    }

}