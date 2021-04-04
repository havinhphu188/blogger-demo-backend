package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.util.mockcustomuser.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleControllerTest {

    private final String DEFAULT_TITLE = "AAAAAA";
    private final String DEFAULT_CONTENT = "BBBBBB";

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void getAll() throws Exception {
        entityManager.persist(createArticle());
        entityManager.persist(createArticle());
        Query query = entityManager.createQuery("SELECT count(a) from Article a where a.author.id = 1");
        int count = ((Number) query.getSingleResult()).intValue();
        assertEquals(2,count);
        mockMvc.perform(get("/api/article/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[1].id").isNumber())
                .andExpect(jsonPath("$.[1].title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.[1].content").value(DEFAULT_CONTENT))
                .andExpect(jsonPath("$.[1].author.name").isString())
                .andExpect(jsonPath("$.[1].author.url").exists())
                .andExpect(jsonPath("$.[1].react").exists());
    }

    @Transactional
    @WithAnonymousUser
    @Test
    void getAllUnauthorizedUser() throws Exception {
        mockMvc.perform(get("/api/article/all"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @WithMockCustomUser(userId = "2")
    @Test
    void getAllWhenNotAuthor() throws Exception {
        entityManager.persist(createArticle());
        entityManager.persist(createArticle());
        Query query = entityManager.createQuery("SELECT count(a) from Article a where a.author.id = 1");
        int count = ((Number) query.getSingleResult()).intValue();
        assertEquals(2,count);
        mockMvc.perform(get("/api/article/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void getGlobalFeed() throws Exception {
        entityManager.persist(createArticle());
        entityManager.persist(createArticle());
        Query query = entityManager.createQuery("SELECT count(a) from Article a where a.author.id = 1");
        int count = ((Number) query.getSingleResult()).intValue();
        assertEquals(2,count);
        mockMvc.perform(get("/api/article/global-feed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[1].id").isNumber())
                .andExpect(jsonPath("$.[1].title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.[1].content").value(DEFAULT_CONTENT))
                .andExpect(jsonPath("$.[1].author.name").isString())
                .andExpect(jsonPath("$.[1].author.url").exists())
                .andExpect(jsonPath("$.[1].react").exists())
                .andExpect(jsonPath("$.[1].reacted").exists());
    }

    @Transactional
    @WithAnonymousUser
    @Test
    void getGlobalFeedWithAnonymous() throws Exception {
        entityManager.persist(createArticle());
        entityManager.persist(createArticle());
        Query query = entityManager.createQuery("SELECT count(a) from Article a where a.author.id = 1");
        int count = ((Number) query.getSingleResult()).intValue();
        assertEquals(2,count);
        mockMvc.perform(get("/api/article/global-feed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[1].id").isNumber())
                .andExpect(jsonPath("$.[1].title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.[1].content").value(DEFAULT_CONTENT))
                .andExpect(jsonPath("$.[1].author.name").isString())
                .andExpect(jsonPath("$.[1].author.url").exists())
                .andExpect(jsonPath("$.[1].react").exists())
                .andExpect(jsonPath("$.[1].reacted").exists());
    }

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void getGlobalFeedWithAuthenticatedUser() throws Exception {
        entityManager.persist(createArticle());
        entityManager.persist(createArticle());
        Query query = entityManager.createQuery("SELECT count(a) from Article a where a.author.id = 1");
        int count = ((Number) query.getSingleResult()).intValue();
        assertEquals(2,count);
        mockMvc.perform(get("/api/article/global-feed"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.[1].id").isNumber())
                .andExpect(jsonPath("$.[1].title").value(DEFAULT_TITLE))
                .andExpect(jsonPath("$.[1].content").value(DEFAULT_CONTENT))
                .andExpect(jsonPath("$.[1].author.name").isString())
                .andExpect(jsonPath("$.[1].author.url").exists())
                .andExpect(jsonPath("$.[1].react").exists())
                .andExpect(jsonPath("$.[1].reacted").exists());
    }

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void testAddArticle() throws Exception {
        Query query = entityManager.createQuery("SELECT count(a) from Article a where a.author.id = 1");
        int numberOfRecordBefore = ((Number) query.getSingleResult()).intValue();

        mockMvc.perform(post("/api/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"AAAAA\",\n" +
                        "    \"content\": \"BBBBB\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("AAAAA"))
                .andExpect(jsonPath("$.content").value("BBBBB"))
                .andExpect(jsonPath("$.author.name").isString())
                .andExpect(jsonPath("$.author.url").exists())
                .andExpect(jsonPath("$.react").exists())
                .andExpect(jsonPath("$.reacted").exists());

        int numberOfRecordAfter = ((Number) query.getSingleResult()).intValue();
        assertEquals(numberOfRecordBefore+1, numberOfRecordAfter);
    }

    @Transactional
    @WithAnonymousUser
    @Test
    void testAddArticleWithAnonymousUser() throws Exception {
        mockMvc.perform(post("/api/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"AAAAA\",\n" +
                        "    \"content\": \"BBBBB\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void testAddArticleWithInvalidForm() throws Exception {
        mockMvc.perform(post("/api/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"\",\n" +
                        "    \"content\": \"\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }




    private Article createArticle(){
        BloggerUser bloggerUser = entityManager.getReference(BloggerUser.class, 1);
        Article article1 = new Article();
        article1.setAuthor(bloggerUser);
        article1.setTitle(DEFAULT_TITLE);
        article1.setContent(DEFAULT_CONTENT);
        return article1;
    }
}