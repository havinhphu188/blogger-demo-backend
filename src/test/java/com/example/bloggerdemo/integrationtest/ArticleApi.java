package com.example.bloggerdemo.integrationtest;

import com.example.bloggerdemo.dto.ArticleDto;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.UUID;

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

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        token = "token";
        when(jwtTokenUtil.getIdFromToken(token)).thenReturn("1");
    }

    @Test
    public void saveArticle() throws Exception {
        String randomTitle = UUID.randomUUID().toString();
        String randomContent = UUID.randomUUID().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer token");
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(randomTitle);
        articleDto.setContent(randomContent);
        HttpEntity<ArticleDto> request = new HttpEntity<>(articleDto,headers);
        ResponseEntity<ArticleDto> response = this.restTemplate.postForEntity("http://localhost:" + port + "/api/article",request,ArticleDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List results = entityManager.createQuery("select a " +
                "from Article a where a.title = :title and a.content = :content")
                .setParameter("title",randomTitle)
                .setParameter("content", randomContent).getResultList();

        assertEquals(results.size(),1);
        assertEquals(((Article)results.get(0)).getTitle(),randomTitle);
        assertEquals(((Article)results.get(0)).getContent(),randomContent);
    }
}