package com.example.bloggerdemo.dto.mapper;

import com.example.bloggerdemo.dto.ArticleDto;
import com.example.bloggerdemo.model.Article;

public class ArticleDtoMapper {
    public static ArticleDto toDto(Article article){
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setTitle(article.getTitle());
        articleDto.setContent(article.getContent());
        articleDto.setAuthor(article.getAuthor().getUsername());
        articleDto.setReact(article.getUserReactions().size());
        return articleDto;
    }

    public static Article toMoDel(ArticleDto articleDto){
        Article article = new Article();
        article.setId(articleDto.getId());
        article.setTitle(articleDto.getTitle());
        article.setContent(articleDto.getContent());
        return article;
    }
}
