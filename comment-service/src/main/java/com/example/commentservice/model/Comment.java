package com.example.bolgwithcontents.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "comment")
public class Comment {
    @Id
    private String commentId;

    @NotNull
    private String content;

    @NotNull
    private String commentTime;

    @NotNull
    private String userName;

    @NotNull
    private String blogId;

    private String rootId;

    private String parentId;

    private int number;

    private Map<String,Comment> reply;

    @NotNull
    private Integer likes;


}
