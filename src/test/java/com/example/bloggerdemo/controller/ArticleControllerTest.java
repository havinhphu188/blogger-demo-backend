package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.service.ArticleService;
import com.example.bloggerdemo.util.mockcustomuser.WithMockCustomUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleController articleController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(articleController).isNotNull();
    }

    @Test
    @WithMockCustomUser(userId = "123")
    void getAll() throws Exception{
        List<Article> articles = new ArrayList<>();
        Article article1 = new Article();
        article1.setId(14);
        article1.setTitle("title");
        article1.setContent("content");
        article1.setAuthor(new BloggerUser());
        article1.getAuthor().setUsername("user1");
        articles.add(article1);
        when(articleService.findAllByUser(123)).thenReturn(articles);
        this.mockMvc.perform(get("/api/article/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id",is(14)));
    }

    @Test
    @WithMockCustomUser(userId = "123")
    void addArticle() throws Exception {

        Article article1 = new Article();
        article1.setTitle("title1");
        article1.setContent("content1");

        Article result = new Article();
        result.setTitle("title1");
        result.setContent("content1");
        result.setAuthor(new BloggerUser());
        result.getAuthor().setId(123);

        when(articleService.save(any(Article.class),eq(123))).thenReturn(result);
        this.mockMvc.perform(post("/api/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"title1\",\n" +
                        "    \"content\": \"content1\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("title1")));
        verify(articleService).save(refEq(article1),eq(123));
    }

    @Test
    @WithMockCustomUser(userId = "123")
    void editArticle() throws Exception {
        Article currentArticle = new Article();
        currentArticle.setId(1);
        currentArticle.setAuthor(new BloggerUser());
        currentArticle.getAuthor().setId(123);

        Article article = new Article();
        article.setId(1);
        article.setTitle("title2");
        article.setContent("content2");

        Article updated = new Article();
        updated.setId(1);
        updated.setTitle("title2");
        updated.setContent("content2");
        updated.setAuthor(new BloggerUser());
        updated.getAuthor().setId(123);
        when(articleRepository.getOne(1)).thenReturn(currentArticle);
        when(articleService.update(refEq(article))).thenReturn(updated);
        this.mockMvc.perform(put("/api/article/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"title2\",\n" +
                        "    \"content\": \"content2\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("title2")));

        verify(this.articleService).update(refEq(article));
    }

    @Test
    @WithMockCustomUser(userId = "144")
    void editArticleAccessDenied() throws Exception {
        Article currentArticle = new Article();
        currentArticle.setId(1);
        currentArticle.setAuthor(new BloggerUser());
        currentArticle.getAuthor().setId(123);

        when(articleRepository.getOne(1)).thenReturn(currentArticle);
        this.mockMvc.perform(put("/api/article/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"title1\",\n" +
                        "    \"content\": \"content2\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser(userId = "123")
    void deleteArticleSuccess() throws Exception{
        Article currentArticle = new Article();
        currentArticle.setId(1);
        currentArticle.setAuthor(new BloggerUser());
        currentArticle.getAuthor().setId(123);

        when(articleRepository.getOne(1)).thenReturn(currentArticle);

        this.mockMvc.perform(delete("/api/article/1"))
                .andDo(print())
                .andExpect(status().isOk());
        verify(articleService).deleteById(1);
    }

    @Test
    @WithMockCustomUser(userId = "144")
    void deleteArticleAccessDenied() throws Exception{
        Article currentArticle = new Article();
        currentArticle.setId(1);
        currentArticle.setAuthor(new BloggerUser());
        currentArticle.getAuthor().setId(123);

        when(articleRepository.getOne(1)).thenReturn(currentArticle);

        this.mockMvc.perform(delete("/api/article/1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}