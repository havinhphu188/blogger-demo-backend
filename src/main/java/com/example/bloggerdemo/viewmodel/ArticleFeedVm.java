package com.example.bloggerdemo.viewmodel;

import com.example.bloggerdemo.model.Article;
import com.example.bloggerdemo.viewmodel.util.ViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter
public class ArticleFeedVm implements ViewModel {
    List<ArticleVm> articleVms;

    public ArticleFeedVm(List<Article> articles, Map<Integer, Boolean>isReactedMap) {
         articleVms = articles.stream()
                .map(article -> {
                    ArticleVm articleVm = new ArticleVm(article);
                    articleVm.setReacted(isReactedMap.get(article.getId()));
                    return articleVm;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ArticleVm> toView() {
        return articleVms;
    }

}
