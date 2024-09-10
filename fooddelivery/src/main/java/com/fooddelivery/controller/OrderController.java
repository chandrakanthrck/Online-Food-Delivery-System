package com.fooddelivery.controller;

import com.fooddelivery.entity.Order;
import com.fooddelivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Asynchronously place an order
    @PostMapping("/place")
    public CompletableFuture<ResponseEntity<Order>> placeOrder(@RequestParam String customerName, @RequestParam String items) {
        return orderService.placeOrderAsync(customerName, items)
                .thenApply(order -> ResponseEntity.ok(order))  // Return the order once completed
                .exceptionally(ex -> ResponseEntity.status(500).body(null));  // Handle any exceptions
    }

    // Get all orders
    @GetMapping("/allorder")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Update the status of an order
    @PutMapping("/updateorder/{orderId}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);  // Return updated order if successful
        } else {
            return ResponseEntity.badRequest().body(null);  // Return 400 if the order update fails
        }
    }
}