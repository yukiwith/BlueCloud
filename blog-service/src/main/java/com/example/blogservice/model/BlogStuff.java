package com.example.bolgwithcontents.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "blog_stuff")
public class BlogStuffConnected {

    @EmbeddedId
    BlogStuffId id;
}
