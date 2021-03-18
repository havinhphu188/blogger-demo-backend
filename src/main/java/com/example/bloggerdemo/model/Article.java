package com.example.bloggerdemo.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;

    @ManyToOne
    private BloggerUser author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserReaction> userReactions = new HashSet<>();

    public void addUserReaction(UserReaction userReaction) {
        userReactions.add( userReaction );
        userReaction.setArticle( this );
    }

    public void removeUserReaction(UserReaction userReaction) {
        userReactions.remove( userReaction );
        userReaction.setArticle( null );
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }
}
