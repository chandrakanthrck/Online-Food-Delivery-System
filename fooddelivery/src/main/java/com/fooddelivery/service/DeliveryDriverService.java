package com.fooddelivery.service;

import com.fooddelivery.entity.DeliveryDriver;
import com.fooddelivery.entity.Order;
import com.fooddelivery.repository.DeliveryDriverRepository;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class DeliveryDriverService {

    // Semaphore to limit the number of drivers working concurrently
    private final Semaphore driverSemaphore = new Semaphore(5);  // Allow 5 concurrent drivers
    private final ReentrantLock driverLock = new ReentrantLock();  // Lock to ensure thread-safe driver assignment

    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Create a delivery driver and mark them as available
    public DeliveryDriver createDriver(String driverName) {
        DeliveryDriver driver = new DeliveryDriver();
        driver.setName(driverName);
        driver.setStatus("Idle");
        driver.setAvailable(true);
        return deliveryDriverRepository.save(driver);
    }

    // Get all available drivers
    public List<DeliveryDriver> getAvailableDrivers() {
        return deliveryDriverRepository.findByAvailable(true);
    }

    // Asynchronously assign a driver to an order
    public CompletableFuture<Order> assignDriverToOrderAsync(Long driverId, Long orderId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Acquire a semaphore permit to limit concurrent drivers
                driverSemaphore.acquire();

                // Lock to prevent race conditions during driver assignment
                driverLock.lock();

                // Find driver and order
                Optional<DeliveryDriver> driverOpt = deliveryDriverRepository.findById(driverId);
                Optional<Order> orderOpt = orderRepository.findById(orderId);

                if (driverOpt.isPresent() && orderOpt.isPresent()) {
                    DeliveryDriver driver = driverOpt.get();
                    Order order = orderOpt.get();

                    // Assign driver to the order
                    driver.setAvailable(false);
                    driver.setStatus("Delivering");
                    order.setAssignedDriver(driver);
                    order.setStatus("Out for Delivery");

                    deliveryDriverRepository.save(driver);
                    return orderRepository.save(order);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Release the semaphore permit and unlock after assignment
                driverLock.unlock();
                driverSemaphore.release();
            }
            return null;
        });
    }

    // Asynchronously complete the delivery of an order
    public CompletableFuture<Order> completeDeliveryAsync(Long orderId) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Order> orderOpt = orderRepository.findById(orderId);
            if (orderOpt.isPresent()) {
                Order order = orderOpt.get();
                DeliveryDriver driver = order.getAssignedDriver();

                // Complete the delivery and free up the driver
                order.setStatus("Delivered");
                driver.setAvailable(true);
                driver.setStatus("Idle");

                deliveryDriverRepository.save(driver);
                return orderRepository.save(order);
            }
            return null;
        });
    }

    // Update driver's availability status
    public DeliveryDriver updateDriverStatus(Long driverId, boolean available) {
        Optional<DeliveryDriver> driverOpt = deliveryDriverRepository.findById(driverId);
        if (driverOpt.isPresent()) {
            DeliveryDriver driver = driverOpt.get();
            driver.setAvailable(available);
            return deliveryDriverRepository.save(driver);
        }
        return null;
    }
}