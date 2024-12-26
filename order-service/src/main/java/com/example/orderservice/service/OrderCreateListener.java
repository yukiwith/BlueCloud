package com.example.bolgwithcontents.service.impl;

import com.example.bolgwithcontents.model.Order;
import com.example.bolgwithcontents.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(topic = "order-create-topic",consumerGroup = "deskill-consumer-group")
public class OrderCreateListener implements RocketMQListener<String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        try {
            Order order = objectMapper.readValue(message, Order.class);
            createOrder(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
    }
}
