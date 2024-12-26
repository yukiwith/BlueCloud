package com.example.bolgwithcontents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
@Table(name = "stuffpicture")
public class StuffPicture {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pictureId;

    @NotNull
    private String pictureUrl;

    @NotNull
    private Long stuffId;

}
