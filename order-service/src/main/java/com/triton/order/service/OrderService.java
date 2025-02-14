package com.triton.order.service;

import com.triton.order.client.InventoryClient;
import com.triton.order.dto.OrderRequestDTO;
import com.triton.order.event.OrderPlacedEvent;
import com.triton.order.model.Order;
import com.triton.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public void placeOrder(OrderRequestDTO orderRequestDTO){
        boolean isProductInStock = inventoryClient.isInStock(orderRequestDTO.skuCode(), orderRequestDTO.quantity());
        if(!isProductInStock){
            throw new RuntimeException("Product with SkuCode " + orderRequestDTO.skuCode() + " is not in stock");
        }

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

        // send the message to Kafka Topic
        OrderPlacedEvent event = new OrderPlacedEvent();
        event.setOrderNumber(order.getOrderNumber());
        event.setEmail(orderRequestDTO.userDetails().email());
        event.setFirstName(orderRequestDTO.userDetails().firstName());
        event.setLastName(orderRequestDTO.userDetails().lastName());
        log.info("Start: message sending to kafka");
        kafkaTemplate.send("order-placed", event);
        log.info("End: message received by kafka");
    }
}
