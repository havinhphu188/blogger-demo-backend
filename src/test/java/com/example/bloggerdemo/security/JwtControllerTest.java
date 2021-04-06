package com.example.bloggerdemo.security;

import com.example.bloggerdemo.controller.BloggerTestBase;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtControllerTest extends BloggerTestBase {

    @Test
    @Transactional
    void createAuthenticationToken() throws Exception{
        JSONObject body = new JSONObject();
        body.put("username","dan_abramov");
        body.put("password", "pw00");

        String content = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JSONObject result = new JSONObject(content);
        String token = (String) result.get("token");

        mockMvc.perform(get("/api/article/subscriptions-feed"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/article/subscriptions-feed")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}