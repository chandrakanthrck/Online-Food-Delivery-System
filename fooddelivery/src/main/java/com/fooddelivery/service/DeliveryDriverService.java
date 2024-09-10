package com.fooddelivery.service;

import com.fooddelivery.entity.DeliveryDriver;
import com.fooddelivery.entity.Order;
import com.fooddelivery.repository.DeliveryDriverRepository;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryDriverService {
    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;
    @Autowired
    private OrderRepository orderRepository;

    public DeliveryDriver createDriver(String driverName){
        DeliveryDriver driver = new DeliveryDriver();
        driver.setName(driverName);
        driver.setStatus("Idle");
        driver.setAvailable(true);
        return deliveryDriverRepository.save(driver);
    }
    public List<DeliveryDriver> getAvailableDrivers(){
        return deliveryDriverRepository.findByAvailable(true);
    }

    public Order assignDriverToOrder(Long driverId, Long orderId){
        Optional<DeliveryDriver> driverOpt = deliveryDriverRepository.findById(driverId);
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if(driverOpt.isPresent() && orderOpt.isPresent()){
            DeliveryDriver driver = driverOpt.get();
            Order order = orderOpt.get();
            driver.setAvailable(false);
            driver.setStatus("Delivering");
            order.setAssignedDriver(driver);
            order.setStatus("Out for Delivery");
            deliveryDriverRepository.save(driver);
            return  orderRepository.save(order);
        }
        return null;
    }

    public Order completeDelivery(Long orderId){
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if(orderOpt.isPresent()){
            Order order = orderOpt.get();
            DeliveryDriver deliveryDriver = order.getAssignedDriver();
            order.setStatus("Delivered");
            deliveryDriver.setAvailable(true);
            deliveryDriver.setStatus("Idle");
            deliveryDriverRepository.save(deliveryDriver);
            return orderRepository.save(order);
        }
        return null;
    }
    public DeliveryDriver updateDriverStatus(Long driverId, boolean available){
        Optional<DeliveryDriver> driverOpt = deliveryDriverRepository.findById(driverId);
        if(driverOpt.isPresent()){
            DeliveryDriver driver = driverOpt.get();
            driver.setAvailable(available);
            return  deliveryDriverRepository.save(driver);
        }
        return null;
    }
}
