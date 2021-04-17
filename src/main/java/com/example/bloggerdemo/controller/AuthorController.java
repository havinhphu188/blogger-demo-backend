package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.service.business.AuthorService;
import com.example.bloggerdemo.viewmodel.AuthorVm;
import com.example.bloggerdemo.viewmodel.util.BloggerResponseEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/author")
@Log4j2
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("get-info/{authorId}")
    public ResponseEntity<Object> getAuthorInfo(@PathVariable int authorId, @AuthenticationPrincipal String userId){
        log.debug("REST request to get author info. AuthorId: {}. userId: {}", authorId, userId);
        BloggerUser author = authorService.getAuthorInfo(authorId);
        boolean isSubscribed = authorService.isUserSubscribeToAuthor(authorId,Integer.parseInt(userId));
        return BloggerResponseEntity.ok(new AuthorVm(author, isSubscribed));
    }

    @PostMapping("subscribe/{authorId}")
    public ResponseEntity<Object> subscribeOrUnsubscribeAuthor(@PathVariable int authorId, @AuthenticationPrincipal String userId){
        log.debug("REST request to subscribeOrUnsubscribeAuthor. authorId: {}. userId: {}", authorId, userId);
        boolean isUserSubscribeToAuthor = authorService.subscribeOrUnsubscribeToAuthor(authorId,Integer.parseInt(userId));
        Map<String, Boolean> result = new HashMap<>();
        result.put("isSubscribed", isUserSubscribeToAuthor);
        return ResponseEntity.ok(result);
    }
}
