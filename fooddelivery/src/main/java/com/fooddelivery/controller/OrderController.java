package com.fooddelivery.controller;

import com.fooddelivery.entity.Order;
import com.fooddelivery.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/place")
    public Order placeOrder(@RequestParam String customerName, @RequestParam String items){
        return orderService.placeOder(customerName, items);
    }
    @GetMapping("/allorder")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }
    @PutMapping("/updateorder/{orderId}")
    public Order updateOrderStatus(@PathVariable  Long orderId, @RequestParam String status){
        return orderService.updateOrderStatus(orderId, status);
    }
}
