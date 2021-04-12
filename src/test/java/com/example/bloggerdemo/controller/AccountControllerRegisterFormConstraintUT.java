package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.service.business.AccountService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerRegisterFormConstraintUT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private JSONObject body;

    @BeforeEach
    void setUp() throws JSONException {
        body = new JSONObject();
        body.put("username","user188Test");
        body.put("password","pw00");
        body.put("displayName","Lovely Cat");
        body.put("bio","Life is a journey");
    }

    @Test
    void happyCase() throws Exception{

        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));
        verify(accountService).registerUser(any(BloggerUser.class));
    }

    @Test
    void invalidUsernameTest() throws Exception {
        body.put("username", "user188Test  ");

        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));
        verify(accountService, never()).registerUser(any(BloggerUser.class));

    }

    @Test
    void invalidUsername2Test() throws Exception {
        body.put("username", "   user188-Test");
        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));
        verify(accountService, never()).registerUser(any(BloggerUser.class));
    }

    @Test
    void invalidUsername3Test() throws Exception {
        body.put("username", "user18 8-Test");
        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));
        verify(accountService, never()).registerUser(any(BloggerUser.class));
    }

    @Test
    void invalidUsername4Test() throws Exception {
        body.put("username", "user188-Test-");
        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));
        verify(accountService, never()).registerUser(any(BloggerUser.class));
    }

    @Test
    void invalidDisplayNameTest() throws Exception {
        body.put("displayName", "  Dan");
        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));

        verify(accountService, never()).registerUser(any(BloggerUser.class));
    }

    @Test
    void invalidDisplayName2Test() throws Exception {
        body.put("displayName", "Dan   ");
        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));

        verify(accountService, never()).registerUser(any(BloggerUser.class));
    }

    @Test
    void invalidDisplayName3Test() throws Exception {
        body.put("displayName", "Dan   Abramov");
        mockMvc.perform(post("/api/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body.toString()));

        verify(accountService, never()).registerUser(any(BloggerUser.class));
    }
}