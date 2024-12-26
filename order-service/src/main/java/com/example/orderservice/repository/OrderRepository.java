package com.example.bolgwithcontents.repository;

import com.example.bolgwithcontents.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
