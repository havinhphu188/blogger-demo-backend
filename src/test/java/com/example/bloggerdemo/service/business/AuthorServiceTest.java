package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthorServiceTest {

    @MockBean
    private BloggerUserRepository bloggerUserRepository;

    @Autowired
    private AuthorService authorService;

    @Test
    void UnsubscribeToAuthor() {
        final int authorId = 1;
        final int userId = 1;

        when(bloggerUserRepository.isUserSubscribeToAuthor(authorId, userId)).thenReturn(true);
        assertFalse(authorService.subscribeOrUnsubscribeToAuthor(authorId, userId));
        verify(bloggerUserRepository).unsubscribeToAuthor(authorId,userId);
    }

    @Test
    void subscribeToAuthor() {
        final int authorId = 1;
        final int userId = 1;

        when(bloggerUserRepository.isUserSubscribeToAuthor(authorId, userId)).thenReturn(false);
        assertTrue(authorService.subscribeOrUnsubscribeToAuthor(authorId, userId));
        verify(bloggerUserRepository).subscribeToAuthor(authorId,userId);
    }

}