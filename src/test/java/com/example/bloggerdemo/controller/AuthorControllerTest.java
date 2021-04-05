package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.Subscription;
import com.example.bloggerdemo.util.mockcustomuser.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthorControllerTest {

    private final String CURRENT_USER_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @WithMockCustomUser(userId = CURRENT_USER_ID)
    @Transactional
    void getCurrentUserInfo() throws Exception {

        int authorId = 2;
        BloggerUser author = entityManager.find(BloggerUser.class, authorId);
        BloggerUser user1 = entityManager.getReference(BloggerUser.class, 1);
        Subscription subscription = new Subscription();
        subscription.setFollower(user1);
        subscription.setFollowee(author);
        entityManager.persist(subscription);

        mockMvc.perform(get("/api/author/get-info/{authorId}", authorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(author.getDisplayName()))
                .andExpect(jsonPath("$.bio").value(author.getBio()))
                .andExpect(jsonPath("$.subscribed").value(true));
    }

    @Test
    @WithAnonymousUser
    void getCurrentUserInfoWithAnonymousUser() throws Exception {
        int authorId = 2;
        mockMvc.perform(get("/api/author/get-info/{authorId}", authorId))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockCustomUser(userId = CURRENT_USER_ID)
    @Transactional
    void subscribeOrUnsubscribeAuthor() throws Exception{
        int authorId = 2;
        Query query = entityManager.createQuery("select sub from Subscription as sub " +
                "where sub.followee.id = :authorId and sub.follower.id = :userId")
                .setParameter("authorId", authorId)
                .setParameter("userId", Integer.valueOf(CURRENT_USER_ID));

        boolean isSubscribed = !query.getResultList().isEmpty();

        mockMvc.perform(post("/api/author/subscribe/{authorId}", authorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSubscribed").value(!isSubscribed));

        boolean isSubscribedAfterApi = !query.getResultList().isEmpty();
        assertTrue(isSubscribedAfterApi);

        mockMvc.perform(post("/api/author/subscribe/{authorId}", authorId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSubscribed").value(isSubscribed));

        boolean isSubscribedAfter2ndApi = !query.getResultList().isEmpty();
        assertFalse(isSubscribedAfter2ndApi);
    }

    @Test
    @WithAnonymousUser
    void subscribeWithAnonymousUser() throws Exception {
        int authorId = 2;
        mockMvc.perform(post("/api/author/subscribe/{authorId}", authorId))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}