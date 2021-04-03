package com.example.bloggerdemo.service.business;

import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @MockBean
    BloggerUserRepository bloggerUserRepository;

    @Test
    void checkIfFieldUnique() {
        when(bloggerUserRepository.existsByDisplayName(anyString())).thenReturn(false);
        boolean result = this.accountService.checkIfFieldUnique("displayName","anyString");
        verify(bloggerUserRepository).existsByDisplayName(anyString());
        assertTrue(result);
    }
}