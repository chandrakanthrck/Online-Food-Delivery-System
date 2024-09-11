package com.fooddelivery.controller;

import com.fooddelivery.entity.DeliveryDriver;
import com.fooddelivery.entity.Order;
import com.fooddelivery.service.DeliveryDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/drivers")
public class DeliveryDriverController {

    @Autowired
    private DeliveryDriverService deliveryDriverService;

    // Create a new driver
    @PostMapping("/create")
    public ResponseEntity<DeliveryDriver> createDriver(@RequestParam String name) {
        DeliveryDriver driver = deliveryDriverService.createDriver(name);
        return ResponseEntity.ok(driver);
    }

    // Get all available drivers
    @GetMapping("/available")
    public ResponseEntity<List<DeliveryDriver>> getAvailableDrivers() {
        List<DeliveryDriver> drivers = deliveryDriverService.getAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }

    // Asynchronously assign a driver to an order
    //Whenever there is an order assign it to the available driver, process will be async
    @PutMapping("/assign/{driverId}/toOrder/{orderId}")
    public CompletableFuture<ResponseEntity<Order>> assignDriverToOrder(@PathVariable Long driverId, @PathVariable Long orderId) {
        return deliveryDriverService.assignDriverToOrderAsync(driverId, orderId)
                .thenApply(order -> ResponseEntity.ok(order))
                .exceptionally(ex -> ResponseEntity.status(500).body(null));  // Handle any exceptions
    }

    // Asynchronously complete the delivery
    //drivers complete the order and go back to available state without
    //waiting for other drivers
    @PutMapping("/completeDelivery/{orderId}")
    public CompletableFuture<ResponseEntity<Order>> completeDelivery(@PathVariable Long orderId) {
        return deliveryDriverService.completeDeliveryAsync(orderId)
                .thenApply(order -> ResponseEntity.ok(order))
                .exceptionally(ex -> ResponseEntity.status(500).body(null));  // Handle any exceptions
    }
}
