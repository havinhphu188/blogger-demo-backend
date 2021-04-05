package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.util.mockcustomuser.WithMockCustomUser;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AccountControllerTest extends BloggerTestBase {

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