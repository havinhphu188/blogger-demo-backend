package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.util.mockcustomuser.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SearchControllerTest extends BloggerTestBase {

    @Test
    @Transactional
    @WithMockCustomUser(userId = CURRENT_USER_ID)
    void searchUserByUsernameTest() throws Exception {
        long count = (long) entityManager.createQuery("select count(a) from BloggerUser a " +
                "where lower(a.displayName) like lower(concat('%', :searchTerm,'%')) ")
                .setParameter("searchTerm","Martin").getSingleResult();

        mockMvc.perform(get("/api/search")
                    .param("searchTerm","Martin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*",hasSize((int) count)))
                .andExpect(jsonPath("$.[0].displayName").exists())
                .andExpect(jsonPath("$.[0].url").exists());
    }
}