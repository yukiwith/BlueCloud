package com.example.bolgwithcontents.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class BlogUpload {

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private String userId;

    @NotNull
    private String userName;

    private List<MultipartFile> picture;
}
