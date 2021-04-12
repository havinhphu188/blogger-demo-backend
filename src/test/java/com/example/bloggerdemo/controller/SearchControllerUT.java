package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerUT {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private BloggerUserRepository bloggerUserRepository;

    @Test
    @WithMockUser
    void searchTermTrimTest() throws Exception{
        mockMvc.perform(get("/api/search")
                .param("searchTerm","     Martin   Fowler    "));
        verify(bloggerUserRepository).findByDisplayNameContainingIgnoreCase("Martin Fowler");
    }
}
