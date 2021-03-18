package com.example.bloggerdemo.service;
import com.example.bloggerdemo.dto.ArticleDto;
import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    void userIdNotExist() {
        int userId = 123;
        ArticleDto articleDto = new ArticleDto();
        when(bloggerUserRepository.findById(userId))
                .thenThrow(new EntityNotFoundException("User not found"));

        assertThrows(EntityNotFoundException.class,()->{
            articleService.save(articleDto,userId);
        });

        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void findAllSuccess() {
        int userId = 123;
        BloggerUser bloggerUser = new BloggerUser();
        bloggerUser.setId(userId);
        when(bloggerUserRepository.findById(123)).thenReturn(Optional.of(bloggerUser));

        articleService.findAllByUser(123);

        verify(articleRepository).findByAuthor(bloggerUser);
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

        verify(articleRepository, never()).findByAuthor(bloggerUser);
    }

    @Test
    void deleteById() {
        Article article = new Article();
        article.setId(10);
        articleService.deleteById(10);
        verify(articleRepository).deleteById(10);
    }
}