package com.example.bolgwithcontents.model;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@Table(name = "blog")
public class Blog implements Serializable {
    @Id
    private String blogId;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Date postTime;

    @NotNull
    private String userName;

    @NotNull
    private String userId;

    // 不写进数据库仅传输用
    @Transient
    private List<String> picture;

    @Transient
    private List<Stuff> stuffList;
    public Blog() {
        this.blogId = UUID.randomUUID().toString();
        this.postTime = new Date();
    }


}
