package com.example.bloggerdemo.viewmodel;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.viewmodel.util.ViewModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorVm implements ViewModel {
    private String name;
    private String bio;
    private boolean subscribed;

    public AuthorVm(BloggerUser bloggerUser, boolean isSubscribed) {
        name = bloggerUser.getDisplayName();
        bio = bloggerUser.getBio();
        subscribed = isSubscribed;
    }

    @Override
    public Object toView() {
        return this;
    }
}
