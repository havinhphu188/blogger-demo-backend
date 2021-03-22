package com.example.bloggerdemo.viewmodel;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.viewmodel.util.ViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ArticleFeedNoReactVm implements ViewModel {
    List<ArticleVm> articleVms;

    public ArticleFeedNoReactVm(List<Article> articles) {
        articleVms = articles.stream()
                .map(ArticleVm::new).collect(Collectors.toList());
    }

    @Override
    public List<ArticleVm> toView() {
        return articleVms;
    }
}
