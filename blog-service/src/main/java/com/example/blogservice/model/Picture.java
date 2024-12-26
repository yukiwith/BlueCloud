package com.example.blogservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "blog_picture")
public class BlogPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pictureId;

    @NotNull
    private String pictureUrl;

    @NotNull
    private String blogId;
}
