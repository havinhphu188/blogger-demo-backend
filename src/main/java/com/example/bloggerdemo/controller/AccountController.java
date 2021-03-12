package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/account")
public class AccountController {

    private final StorageService storageService;

    @Autowired
    public AccountController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping()
    public ResponseEntity<?> handleFileUpload(@RequestParam("filexx") MultipartFile file) {
        storageService.store(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
