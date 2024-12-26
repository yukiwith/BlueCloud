package com.example.bolgwithcontents.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    private String orderId;

    @NotNull
    private String userId;

    @NotNull
    private Long stuffId;

    @NotNull
    private Date date;
}
