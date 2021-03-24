package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.service.BloggerUserService;
import com.example.bloggerdemo.viewmodel.AuthorVm;
import com.example.bloggerdemo.viewmodel.util.BloggerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/author")
public class AuthorController {

    private final BloggerUserService bloggerUserService;

    @Autowired
    public AuthorController(BloggerUserService bloggerUserService) {
        this.bloggerUserService = bloggerUserService;
    }

    @GetMapping("get-info/{authorId}")
    public ResponseEntity<?> getCurrentUserInfo(@PathVariable int authorId){
        BloggerUser author = bloggerUserService.getAuthorInfo(authorId);
        return BloggerResponseEntity.ok(new AuthorVm(author));
    }
}
