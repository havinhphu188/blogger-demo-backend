package com.example.bloggerdemo.viewmodel;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.viewmodel.util.ViewModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleVm implements ViewModel {
    private Integer id;
    private String title;
    private String content;
    private String author;
    private int react;
    private boolean reacted;

    public ArticleVm(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = article.getAuthor().getUsername();
        this.react = article.getUserReactions().size();
    }

    @Override
    public Object toView() {
        return this;
    }
}
