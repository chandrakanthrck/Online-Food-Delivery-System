package com.fooddelivery.service;

import com.fooddelivery.entity.Chef;
import com.fooddelivery.entity.Order;
import com.fooddelivery.repository.ChefRepository;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChefService {
    @Autowired
    private ChefRepository chefRepository;
    @Autowired
    private OrderRepository orderRepository;

    public Chef createChef(String name){
        Chef chef = new Chef();
        chef.setName(name);
        chef.setAvailable(true);
        return chefRepository.save(chef);
    }

    public List<Chef> getAvailableChefs(){
        return chefRepository.findByAvailable(true);
    }
    //assign a chef to the order
    public Order assignChefToOrder(Long chefId, Long orderId){
        Optional<Chef> chefOpt = chefRepository.findById(chefId);
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if(chefOpt.isPresent() && orderOpt.isPresent()){
            Chef chef = chefOpt.get();
            Order order = orderOpt.get();
            chef.setAvailable(false);
            order.setAssignedChef(chef);
            order.setStatus("Assigned to Chef");
            chefRepository.save(chef);
            return orderRepository.save(order);
        }
        return null;
    }

    public Order completeOrder(Long orderId){
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if(orderOpt.isPresent()){
            Order order = orderOpt.get();
            Chef chef = order.getAssignedChef();
            order.setStatus("Prepared");
            chef.setAvailable(true);
            chefRepository.save(chef);
            return orderRepository.save(order);
        }
        return null;
    }

    public Chef updateChefStatus(Long chefId, boolean available){
        Optional<Chef> chefOpt = chefRepository.findById(chefId);
        if(chefOpt.isPresent()){
            Chef chef = chefOpt.get();
            chef.setAvailable(available);
            return chefRepository.save(chef);
        }
        return null;
    }

}
