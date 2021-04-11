package com.example.bloggerdemo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Content is mandatory")
    @Column(length = 1000)
    private String content;

    @ManyToOne
    private BloggerUser author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserReaction> userReactions = new HashSet<>();

    private LocalDateTime createAt = LocalDateTime.now();

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
