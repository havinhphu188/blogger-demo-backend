package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import com.example.bloggerdemo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/account")
public class AccountController {

    private final StorageService storageService;
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public AccountController(StorageService storageService
            , BloggerUserRepository bloggerUserRepository) {
        this.storageService = storageService;
        this.bloggerUserRepository = bloggerUserRepository;
    }

    @PostMapping()
    public ResponseEntity<?> handleFileUpload(@RequestParam("filexx") MultipartFile file) {
        storageService.store(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("user-info")
    public ResponseEntity<?> getCurrentUserInfo(@AuthenticationPrincipal String userId){
        BloggerUser bloggerUser = this.bloggerUserRepository
                .findById(Integer.parseInt(userId))
                .orElseThrow(() -> new EntityNotFoundException("userid not found: " + userId));
        Map<String, String> result = new HashMap<>();
        result.put("username", bloggerUser.getDisplayName());
        return ResponseEntity.ok(result);
    }
}
