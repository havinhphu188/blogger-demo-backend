package com.example.bloggerdemo.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class BloggerUser  {
    @JsonValue
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    private String password;

}
