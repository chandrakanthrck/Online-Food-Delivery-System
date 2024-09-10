package com.fooddelivery.service;

import com.fooddelivery.entity.Order;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // Asynchronous method for placing an order
    public CompletableFuture<Order> placeOrderAsync(String customerName, String items) {
        return CompletableFuture.supplyAsync(() -> {
            Order order = new Order();
            order.setCustomerName(customerName);
            order.setItems(items);
            order.setStatus("Preparing");

            // Simulate order placement delay
            try {
                Thread.sleep(2000);  // Simulate a delay for placing an order
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return orderRepository.save(order);  // Save the order in the database
        });
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long id, String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }
}