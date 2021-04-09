package com.example.bloggerdemo.viewmodel;

import com.example.bloggerdemo.model.BloggerUser;
import com.example.bloggerdemo.viewmodel.util.ViewModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorPreviewVm implements ViewModel {
    private String displayName;
    private String bio;
    private String url;

    public AuthorPreviewVm(BloggerUser bloggerUser) {
        this.displayName = bloggerUser.getDisplayName();
        this.bio = bloggerUser.getBio();
        this.url = String.valueOf(bloggerUser.getId());
    }

    @Override
    public Object toView() {
        return this;
    }
}
