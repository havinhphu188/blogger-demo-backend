package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import com.example.bloggerdemo.service.AccountService;
import com.example.bloggerdemo.service.StorageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/account")
public class AccountController {

    private final StorageService storageService;
    private final BloggerUserRepository bloggerUserRepository;
    private final AccountService accountService;

    @Autowired
    public AccountController(StorageService storageService,
                             BloggerUserRepository bloggerUserRepository, AccountService accountService) {
        this.storageService = storageService;
        this.bloggerUserRepository = bloggerUserRepository;
        this.accountService = accountService;
    }

    @PostMapping()
    public ResponseEntity<?> handleFileUpload(@RequestParam("filexx") MultipartFile file) {
        storageService.store(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("user-info")
    public ResponseEntity<?> getCurrentUserInfo(@AuthenticationPrincipal String userId){
        if (userId.equals("anonymousUser")) return new ResponseEntity<>(HttpStatus.OK);
        BloggerUser bloggerUser = this.bloggerUserRepository
                .findById(Integer.parseInt(userId))
                .orElseThrow(() -> new EntityNotFoundException("userid not found: " + userId));
        Map<String, String> result = new HashMap<>();
        result.put("username", bloggerUser.getDisplayName());
        return ResponseEntity.ok(result);
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@RequestBody UserParam user){
        BloggerUser bloggerUser = new BloggerUser();
        bloggerUser.setUsername(user.getUsername());
        bloggerUser.setDisplayName(user.getDisplayName());
        bloggerUser.setBio(user.getBio());
        bloggerUser.setPassword(user.getPassword());

        this.accountService.registerUser(bloggerUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("check-if-username-unique")
    public ResponseEntity<?> isUsernameUnique(@RequestParam("username")String username){
        boolean result = this.accountService.isUsernameUnique(username);
        return ResponseEntity.ok(result);
    }


}

@Getter
@Setter
class UserParam{
    @NotNull
    private String username;
    @NotNull
    private String password;

    private String displayName;

    private String bio;

}
