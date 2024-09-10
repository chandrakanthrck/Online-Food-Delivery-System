package com.fooddelivery.controller;

import com.fooddelivery.entity.DeliveryDriver;
import com.fooddelivery.entity.Order;
import com.fooddelivery.service.DeliveryDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DeliveryDriverController {
    @Autowired
    private DeliveryDriverService deliveryDriverService;

    @PostMapping("/create")
    public DeliveryDriver createDriver(@RequestParam String name){
        return deliveryDriverService.createDriver(name);
    }
    @GetMapping("/available")
    public List<DeliveryDriver> getDrivers(){
        return deliveryDriverService.getAvailableDrivers();
    }
    @PutMapping("/assign/{driverId}/toOrder/{orderId}")
    public Order assignOrderToDriver(@PathVariable Long driverId, @PathVariable Long orderId){
        return deliveryDriverService.assignDriverToOrder(driverId, orderId);
    }
    @PutMapping("/completeDelivery/{orderId}")
    public Order completeDelivery(@PathVariable Long orderId) {
        return deliveryDriverService.completeDelivery(orderId);
    }
}
