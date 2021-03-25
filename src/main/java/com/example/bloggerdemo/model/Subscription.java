package com.example.bloggerdemo.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private BloggerUser follower;

    @ManyToOne(fetch = FetchType.LAZY)
    private BloggerUser followee;

}
