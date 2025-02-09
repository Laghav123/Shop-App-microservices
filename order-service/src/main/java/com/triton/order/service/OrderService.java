package com.triton.order.service;

import com.triton.order.dto.OrderRequestDTO;
import com.triton.order.model.Order;
import com.triton.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequestDTO orderRequestDTO){
        // map orderRequest to order object
        Order order = new Order(
                orderRequestDTO.id(),
                UUID.randomUUID().toString(),
                orderRequestDTO.skuCode(),
                orderRequestDTO.price(),
                orderRequestDTO.quantity()
        );

        // save order to orderRepository
        orderRepository.save(order);
    }
}
