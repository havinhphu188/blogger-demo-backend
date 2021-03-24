package com.example.bloggerdemo.viewmodel;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.viewmodel.util.ViewModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorVm implements ViewModel {
    private String name;

    public AuthorVm(BloggerUser bloggerUser) {
        name = bloggerUser.getUsername();
    }

    @Override
    public Object toView() {
        return this;
    }
}
