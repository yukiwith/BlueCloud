package com.example.bolgwithcontents.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "stuff")

public class Stuff implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stuffId;

    @NotNull
    private Integer price;

    @NotNull
    private String name;

    @NotNull
    private String brand;

    @NotNull
    private Integer stock;


    @NotNull
    private Date start;

    private Date deadline;

    @Transient
    private List<String> stuffPicture;

}
