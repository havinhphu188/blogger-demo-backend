package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.Subscription;
import com.example.bloggerdemo.util.mockcustomuser.WithMockCustomUser;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AccountControllerTest extends BloggerTestBase {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @WithMockCustomUser(userId = CURRENT_USER_ID)
    @Test
    void getSubscribedAuthor() throws Exception {
        BloggerUser currentUser = entityManager.find(BloggerUser.class, Integer.parseInt(CURRENT_USER_ID));

        Subscription sub1 = new Subscription();
        sub1.setFollower(entityManager.getReference(BloggerUser.class,Integer.parseInt(CURRENT_USER_ID)));
        sub1.setFollowee(entityManager.getReference(BloggerUser.class,2));

        Subscription sub2 = new Subscription();
        sub2.setFollower(entityManager.getReference(BloggerUser.class,Integer.parseInt(CURRENT_USER_ID)));
        sub2.setFollowee(entityManager.getReference(BloggerUser.class,3));

        entityManager.persist(sub1);
        entityManager.persist(sub2);

        long subscriptionsOfUser = (long) entityManager
                .createQuery("select count(sub) from Subscription sub " +
                        "where sub.follower.id = :userId")
                .setParameter("userId", Integer.parseInt(CURRENT_USER_ID)).getSingleResult();
        mockMvc.perform(get("/api/account/subscribed-author"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(currentUser.getDisplayName()))
                .andExpect(jsonPath("$.subscribedAuthors", hasSize((int) subscriptionsOfUser)))
                .andExpect(jsonPath("$.subscribedAuthors.[0].displayName").isString())
                .andExpect(jsonPath("$.subscribedAuthors.[0].bio").isString())
                .andExpect(jsonPath("$.subscribedAuthors.[0].url").exists())
        ;
    }

    @Test
    @WithAnonymousUser
    void getUserInfoFromAnonymousUser() throws Exception {
        mockMvc.perform(get("/api/account/user-info"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(userId = CURRENT_USER_ID)
    void getUserInfoAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/account/user-info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username 1"));
    }

    @Test
    @Transactional
    void registerUser() throws Exception {
        JSONObject body = new JSONObject();
        body.put("username","user188Test");
        body.put("password","pw00");
        body.put("displayName","Lovely Cat");
        body.put("bio","Life is a journey");

        mockMvc.perform(post("/api/account/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body.toString()))
                .andDo(print())
                .andExpect(status().isOk());

        BloggerUser user = (BloggerUser) entityManager
                .createQuery("select u from BloggerUser u where u.username = :username")
                .setParameter("username", "user188Test").getSingleResult();

        assertTrue(passwordEncoder.matches("pw00", user.getPassword()));
        assertEquals("Lovely Cat",user.getDisplayName());
        assertEquals("Life is a journey", user.getBio());

    }

    @Test
    @Transactional
    void isUsernameUnique() throws Exception{
        final String USERNAME = "AXXAAXX";
        final String DISPLAY_NAME = "KKOOFFF";

        mockMvc.perform(
                get("/api/account/check-if-field-unique")
                        .param("fieldName", "username")
                        .param("fieldValue", USERNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        mockMvc.perform(
                get("/api/account/check-if-field-unique")
                        .param("fieldName", "displayName")
                        .param("fieldValue", DISPLAY_NAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));

        BloggerUser bloggerUser = new BloggerUser();
        bloggerUser.setUsername(USERNAME);
        bloggerUser.setPassword("efwfwscdc");
        bloggerUser.setDisplayName(DISPLAY_NAME);
        bloggerUser.setBio("eewfefw");

        entityManager.persist(bloggerUser);

        mockMvc.perform(
                get("/api/account/check-if-field-unique")
                        .param("fieldName", "username")
                        .param("fieldValue", USERNAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));

        mockMvc.perform(
                get("/api/account/check-if-field-unique")
                        .param("fieldName", "displayName")
                        .param("fieldValue", DISPLAY_NAME))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }
}