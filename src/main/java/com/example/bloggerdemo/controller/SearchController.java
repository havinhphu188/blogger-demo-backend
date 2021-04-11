package com.example.bloggerdemo.controller;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import com.example.bloggerdemo.viewmodel.AuthorSearchResultVm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/search")
public class SearchController {

    private final BloggerUserRepository bloggerUserRepository;

    public SearchController(BloggerUserRepository bloggerUserRepository) {
        this.bloggerUserRepository = bloggerUserRepository;
    }

    @GetMapping()
    public ResponseEntity<List<AuthorSearchResultVm>> searchUserByUsernames(@RequestParam("searchTerm") String searchTerm){
        String preparedSearchTerm = searchTerm.replaceAll("\\s{2,}", " ").trim();
        List<BloggerUser> searchResult = bloggerUserRepository
                .findByDisplayNameContainingIgnoreCase
                        (preparedSearchTerm);
        List<AuthorSearchResultVm> resultViewModel = searchResult.stream()
                .map(AuthorSearchResultVm::new).collect(Collectors.toList());
        return ResponseEntity.ok(resultViewModel);
    }
}
