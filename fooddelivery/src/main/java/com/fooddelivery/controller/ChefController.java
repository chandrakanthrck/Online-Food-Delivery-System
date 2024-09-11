package com.fooddelivery.controller;

import com.fooddelivery.entity.Chef;
import com.fooddelivery.entity.Order;
import com.fooddelivery.service.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/chefs")
public class ChefController {

    @Autowired
    private ChefService chefService;

    // Create a new chef
    @PostMapping("/create")
    public ResponseEntity<Chef> createChef(@RequestParam String name) {
        Chef chef = chefService.createChef(name);
        return ResponseEntity.ok(chef);  // Return the created chef
    }

    // Asynchronously assign a chef to an order
    //Assigning an available chef for the order
    @PutMapping("/assign/{chefId}/toOrder/{orderId}")
    public CompletableFuture<ResponseEntity<Order>> assignChefToOrder(@PathVariable Long chefId, @PathVariable Long orderId) {
        return chefService.assignChefToOrderAsync(chefId, orderId)
                .thenApply(order -> ResponseEntity.ok(order))  // Return the order once assigned
                .exceptionally(ex -> ResponseEntity.status(500).body(null));  // Handle exceptions
    }

    // Asynchronously complete the order
    //once each chef finishes his task, he will be in the queue
    //for taking the next order
    @PutMapping("/completeOrder/{orderId}")
    public CompletableFuture<ResponseEntity<Order>> completeOrder(@PathVariable Long orderId) {
        return chefService.completeOrderAsync(orderId)
                .thenApply(order -> ResponseEntity.ok(order))  // Return the order once completed
                .exceptionally(ex -> ResponseEntity.status(500).body(null));  // Handle exceptions
    }
}
