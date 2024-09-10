package com.fooddelivery.service;

import com.fooddelivery.entity.Order;
import com.fooddelivery.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    public Order placeOder(String customerName, String items){
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setItems(items);
        order.setStatus("Preparing");
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders(){
        return  orderRepository.findAll();
    }

    public Order updateOrderStatus(Long id, String status){
        Optional<Order> orderOpt = orderRepository.findById(id);
        if(orderOpt.isPresent()){
            Order order = orderOpt.get();
            order.setStatus(status);
            return orderRepository.save(order);
        }
        return null;
    }
}
