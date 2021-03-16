package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleApi {
    @LocalServerPort
    private int port;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TestRestTemplate restTemplate;
    private String token;

    @BeforeEach
    public void setUp() {
        token = "token";
        when(jwtTokenUtil.getIdFromToken(token)).thenReturn("1");
    }

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer token");
        Article article = new Article();
        article.setTitle("abc");
        article.setContent("contentabc");
        HttpEntity<Article> request = new HttpEntity<>(article,headers);
        ResponseEntity<Article> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/article",request,Article.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
