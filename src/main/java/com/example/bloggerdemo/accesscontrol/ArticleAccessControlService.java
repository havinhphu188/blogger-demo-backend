package com.example.bloggerdemo.accesscontrol;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ArticleAccessControlService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleAccessControlService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public boolean isAccessible(int id){
        Article article = articleRepository.getOne(id);
        if (article.getAuthor().getId() == getUserIdFromContext()) return true;
        return false;
    }

    private int getUserIdFromContext(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Integer.parseInt((String) securityContext.getAuthentication().getPrincipal());
    }
}
