package com.fooddelivery.controller;

import com.fooddelivery.entity.Chef;
import com.fooddelivery.entity.Order;
import com.fooddelivery.service.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chefs")
public class ChefController {
    @Autowired
    private ChefService chefService;

    @PostMapping("/create")
    public Chef createChef(@RequestParam String name){
        return chefService.createChef(name);
    }
    @PutMapping("/assign/{chefId}/toOrder/{orderId}")
    public Order assignChefToOrder(@PathVariable Long chefId, @PathVariable Long orderId){
        return chefService.assignChefToOrder(chefId,orderId);
    }
    @PutMapping("/completeOrder/{orderId}")
    public Order completeOrder(@PathVariable Long orderId){
        return chefService.completeOrder(orderId);
    }
}
