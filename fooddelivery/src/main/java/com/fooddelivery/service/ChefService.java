package com.fooddelivery.service;

import com.fooddelivery.entity.Chef;
import com.fooddelivery.entity.Order;
import com.fooddelivery.repository.ChefRepository;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ChefService {

    private final ReentrantLock chefLock = new ReentrantLock();  // Lock for chef assignment
    private final Semaphore chefSemaphore = new Semaphore(3);  // Limit to 3 concurrent chefs

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Create a chef and mark them as available
    public Chef createChef(String name) {
        Chef chef = new Chef();
        chef.setName(name);
        chef.setAvailable(true);
        return chefRepository.save(chef);
    }

    // Get all available chefs
    public List<Chef> getAvailableChefs() {
        return chefRepository.findByAvailable(true);
    }

    // Asynchronously assign a chef to an order
    public CompletableFuture<Order> assignChefToOrderAsync(Long chefId, Long orderId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Chef> chefOpt = chefRepository.findById(chefId);
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (chefOpt.isPresent() && orderOpt.isPresent()) {
                try {
                    // Semaphore controls concurrent chefs working
                    chefSemaphore.acquire();
                    // Lock to prevent race conditions
                    chefLock.lock();

                    Chef chef = chefOpt.get();
                    Order order = orderOpt.get();

                    chef.setAvailable(false);  // Mark chef as unavailable (busy)
                    order.setAssignedChef(chef);
                    order.setStatus("Assigned to Chef");

                    chefRepository.save(chef);
                    return orderRepository.save(order);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    chefLock.unlock();
                    chefSemaphore.release();
                }
            }
            return null;
        });
    }

    // Asynchronously mark an order as complete and make the chef available again
    public CompletableFuture<Order> completeOrderAsync(Long orderId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                Chef chef = order.getAssignedChef();
                order.setStatus("Prepared");
                chef.setAvailable(true);  // Mark chef as available again after completing the order
                chefRepository.save(chef);

                return orderRepository.save(order);
            }
            return null;
        });
    }

    // Update chef's availability status
    public Chef updateChefStatus(Long chefId, boolean available) {
        Optional<Chef> chefOpt = chefRepository.findById(chefId);
        if (chefOpt.isPresent()) {
            Chef chef = chefOpt.get();
            chef.setAvailable(available);
            return chefRepository.save(chef);
        }
        return null;
    }
}
