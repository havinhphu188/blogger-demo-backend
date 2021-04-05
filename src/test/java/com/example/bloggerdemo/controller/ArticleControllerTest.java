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
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArticleControllerTest {

    private final String DEFAULT_TITLE = "AAAAAA";
    private final String DEFAULT_CONTENT = "BBBBBB";

    private final String UPDATED_TITLE = "UPAAAAAA";
    private final String UPDATED_CONTENT = "UPBBBBBB";

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

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void testAddOrRemoveReact() throws Exception {
        Article article = createArticle();
        entityManager.persist(article);
        int articleId = article.getId();
        Query query = entityManager.createQuery("select count(r) from UserReaction r where r.article.id = :articleId")
                .setParameter("articleId", articleId);
        final long originalCountOfReact = (long) query.getSingleResult();

        mockMvc.perform(post("/api/article/react/{articleId}",articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"\",\n" +
                        "    \"content\": \"\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isReacted").value(true))
                .andExpect(jsonPath("$.reaction").value(originalCountOfReact + 1));

        long afterApiCallReact = (long)query.getSingleResult();
        assertEquals(originalCountOfReact+1,afterApiCallReact);

        mockMvc.perform(post("/api/article/react/{articleId}",articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \"\",\n" +
                        "    \"content\": \"\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isReacted").value(false))
                .andExpect(jsonPath("$.reaction").value(originalCountOfReact));

        afterApiCallReact = (long)query.getSingleResult();
        assertEquals(originalCountOfReact,afterApiCallReact);
    }

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void testUpdateArticle() throws Exception {

        Article article = createArticle();
        entityManager.persist(article);
        int articleId = article.getId();

        mockMvc.perform(put("/api/article/{articleId}", articleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"title\": \""+ UPDATED_TITLE +"\",\n" +
                        "    \"content\": \""+ UPDATED_CONTENT +"\"\n" +
                        "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value(UPDATED_TITLE))
                .andExpect(jsonPath("$.content").value(UPDATED_CONTENT))
                .andExpect(jsonPath("$.author.name").isString())
                .andExpect(jsonPath("$.author.url").exists())
                .andExpect(jsonPath("$.react").exists())
                .andExpect(jsonPath("$.reacted").exists());

        Article updatedArticle = entityManager.find(Article.class,articleId);
        assertEquals(UPDATED_TITLE, updatedArticle.getTitle());
        assertEquals(UPDATED_CONTENT, updatedArticle.getContent());
    }

    @Transactional
    @WithMockCustomUser(userId = "1")
    @Test
    void testDeleteArticle() throws Exception {
        Article article = createArticle();
        entityManager.persist(article);
        int articleId = article.getId();

        mockMvc.perform(delete("/api/article/{articleId}", articleId))
                .andDo(print())
                .andExpect(status().isOk());

        Article updatedArticle = entityManager.find(Article.class, articleId);
        assertNull(updatedArticle);
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