package com.example.bloggerdemo.viewmodel;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.viewmodel.util.ViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ArticleVm implements ViewModel {
    private Integer id;
    private String title;
    private String content;
    private Map<String, String> author;
    private int react;
    private boolean reacted;
    private String url;

    public ArticleVm(Article article){
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.author = new HashMap<>();
        author.put("name", article.getAuthor().getDisplayName());
        author.put("url", String.valueOf(article.getAuthor().getId()));
        this.react = article.getUserReactions().size();
    }

    @Override
    public Object toView() {
        return this;
    }
}
