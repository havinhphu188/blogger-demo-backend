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
    public ResponseEntity<?> getCurrentUserInfo(@RequestParam("searchTerm") String searchTerm){
        List<BloggerUser> searchResult = bloggerUserRepository.findByDisplayNameContainingIgnoreCase(searchTerm);
        List<AuthorSearchResultVm> resultViewModel = searchResult.stream()
                .map(AuthorSearchResultVm::new).collect(Collectors.toList());
        return ResponseEntity.ok(resultViewModel);
    }
}
