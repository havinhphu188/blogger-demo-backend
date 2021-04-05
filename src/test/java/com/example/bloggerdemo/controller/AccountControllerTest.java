package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.util.mockcustomuser.WithMockCustomUser;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @PersistenceContext
    EntityManager entityManager;

    private final String CURRENT_USER_ID = "1";

    @Transactional
    @WithMockCustomUser(userId = CURRENT_USER_ID)
    @Test
    void getCurrentUserInfo() throws Exception {
        BloggerUser currentUser = entityManager.find(BloggerUser.class, Integer.parseInt(CURRENT_USER_ID));

        mockMvc.perform(get("/api/account/user-info"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(currentUser.getDisplayName()));
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
    }

    @Test
    void isUsernameUnique() {
    }
}