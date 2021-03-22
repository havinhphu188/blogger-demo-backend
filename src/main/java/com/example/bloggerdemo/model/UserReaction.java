package com.example.bloggerdemo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"article_id", "blogger_user_id"})})
public class UserReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String mark;

    @ManyToOne
    private Article article;

    @ManyToOne
    private BloggerUser bloggerUser;

}
