package com.example.bloggerdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDto {
    private int id;

    private String title;

    private String content;

    private String author;

    private int react;
}
