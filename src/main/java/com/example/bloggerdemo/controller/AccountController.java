package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import com.example.bloggerdemo.service.business.AccountService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/account")
public class AccountController {
    private final BloggerUserRepository bloggerUserRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountController(BloggerUserRepository bloggerUserRepository,
                             AccountService accountService,
                             PasswordEncoder passwordEncoder) {
        this.bloggerUserRepository = bloggerUserRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("user-info")
    public ResponseEntity<Map<String, String>> getCurrentUserInfo(@AuthenticationPrincipal String userIdString){
        int userId;
        try {
            userId = Integer.parseInt(userIdString);
        } catch (NumberFormatException exception) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        BloggerUser bloggerUser = this.bloggerUserRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("userid not found: " + userId));
        Map<String, String> result = new HashMap<>();
        result.put("username", bloggerUser.getDisplayName());
        return ResponseEntity.ok(result);
    }

    @PostMapping("register")
    public ResponseEntity<Object> registerUser(@RequestBody UserParam user){
        BloggerUser bloggerUser = new BloggerUser();
        bloggerUser.setUsername(user.getUsername());
        bloggerUser.setDisplayName(user.getDisplayName());
        bloggerUser.setBio(user.getBio());
        bloggerUser.setPassword(passwordEncoder.encode(user.getPassword()));

        this.accountService.registerUser(bloggerUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("check-if-field-unique")
    public ResponseEntity<Boolean> isUsernameUnique(@RequestParam("fieldName")String fieldName,
                                              @RequestParam("fieldValue")String fieldValue){
        boolean result = this.accountService.checkIfFieldUnique(fieldName, fieldValue);
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
    @NotNull
    private String displayName;

    private String bio;

}
