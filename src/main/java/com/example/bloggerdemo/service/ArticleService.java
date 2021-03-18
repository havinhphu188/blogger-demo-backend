package com.example.bloggerdemo.service;

import com.example.bloggerdemo.dto.ArticleDto;
import com.example.bloggerdemo.dto.mapper.ArticleDtoMapper;
import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.model.UserReaction;
import com.example.bloggerdemo.repository.ArticleRepository;
import com.example.bloggerdemo.repository.BloggerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final BloggerUserRepository bloggerUserRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, BloggerUserRepository bloggerUserRepository) {
        this.articleRepository = articleRepository;
        this.bloggerUserRepository = bloggerUserRepository;
    }

    public ArticleDto save(ArticleDto articleDto, int userId){
        Article article = ArticleDtoMapper.toMoDel(articleDto);
        article.setAuthor(getBloggerUser(userId));
        return ArticleDtoMapper.toDto(articleRepository.save(article));
    }

    public List<ArticleDto> findAllByUser(int userId) {
        return articleRepository
                .findByAuthor(getBloggerUser(userId))
                .stream().map(ArticleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    private BloggerUser getBloggerUser(int userId){
        return bloggerUserRepository
                .findById(userId).orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

    }

    public ArticleDto update(ArticleDto articleDto) {
        Article current = articleRepository.getOne(articleDto.getId());
        current.update(
                articleDto.getTitle(),
                articleDto.getContent());
        return ArticleDtoMapper
                .toDto(
                        articleRepository.save(current)
                );
    }

    public void deleteById(Integer id) {
        this.articleRepository.deleteById(id);
    }

    public List<ArticleDto> getGlobalFeed() {
        return articleRepository.findAll()
                .stream().map(ArticleDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addUserReaction(int articleId){
        Article article = this.articleRepository.getOne(articleId);
        article.addUserReaction(new UserReaction());
        this.articleRepository.save(article);
    }
}

