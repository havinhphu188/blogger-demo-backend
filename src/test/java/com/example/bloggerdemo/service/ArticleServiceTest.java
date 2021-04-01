package com.example.bloggerdemo.service;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import com.example.bloggerdemo.service.business.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private BloggerUserRepository bloggerUserRepository;

    @BeforeEach
    void beforeEach(){
        this.articleService = new ArticleService(articleRepository, bloggerUserRepository);
    }

    @Test
    void saveSuccess() {
        int userId = 123;
        Article article = new Article();
        BloggerUser bloggerUser = new BloggerUser();
        bloggerUser.setId(userId);
        when(bloggerUserRepository.findById(userId))
                .thenReturn(Optional.of(bloggerUser));

        articleService.save(article,userId);

        verify(articleRepository).save(article);
        assertEquals(123,article.getAuthor().getId());
    }

    @Test
    void userIdNotExist() {
        int userId = 123;
        Article article = new Article();
        when(bloggerUserRepository.findById(userId))
                .thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class,()->{
            articleService.save(article,userId);
        });

        verify(articleRepository, never()).save(article);
    }

    @Test
    void findAllSuccess() {
        int userId = 123;
        BloggerUser bloggerUser = new BloggerUser();
        bloggerUser.setId(userId);
        when(bloggerUserRepository.findById(123)).thenReturn(Optional.of(bloggerUser));

        articleService.findAllByUser(123);

        verify(articleRepository).findByAuthorOrderByCreateAtDesc(bloggerUser);
    }

    @Test
    void findAllUserNotExist() {
        int userId = 123;
        BloggerUser bloggerUser = new BloggerUser();
        bloggerUser.setId(userId);
        when(bloggerUserRepository.findById(123)).thenThrow(new EntityNotFoundException());
        assertThrows(EntityNotFoundException.class,()->{
            articleService.findAllByUser(123);
        });

        verify(articleRepository, never()).findByAuthorOrderByCreateAtDesc(bloggerUser);
    }

    @Test
    void updateSuccess() {
        Article current = new Article();
        current.setId(12);
        current.setAuthor(new BloggerUser());
        current.getAuthor().setId(12);
        current.setTitle("title1");
        current.setContent("content1");

        Article article = new Article();
        article.setId(12);
        article.setTitle("title2");
        article.setContent("content2");

        when(articleRepository.getOne(12)).thenReturn(current);

        articleService.update(article);

        verify(articleRepository).save(current);
        assertEquals(current.getTitle(),"title2");
        assertEquals(current.getContent(),"content2");
    }

    @Test
    void deleteById() {
        Article article = new Article();
        article.setId(10);
        articleService.deleteById(10);
        verify(articleRepository).deleteById(10);
    }
}