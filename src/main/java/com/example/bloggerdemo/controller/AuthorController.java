package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.service.AuthorService;
import com.example.bloggerdemo.viewmodel.AuthorVm;
import com.example.bloggerdemo.viewmodel.util.BloggerResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/author")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("get-info/{authorId}")
    public ResponseEntity<?> getCurrentUserInfo(@PathVariable int authorId){
        BloggerUser author = authorService.getAuthorInfo(authorId);
        return BloggerResponseEntity.ok(new AuthorVm(author));
    }

    @PostMapping("subscribe/{authorId}")
    public ResponseEntity<?> addReaction(@PathVariable int authorId, @AuthenticationPrincipal String userId){
        boolean isUserSubscribeToAuthor = authorService.subscribeToAuthor(authorId,Integer.parseInt(userId));
        Map<String, Boolean> result = new HashMap<>();
        result.put("isSubscribed", isUserSubscribeToAuthor);
        return ResponseEntity.ok(result);
    }
}
