package com.fooddelivery.repository;

import com.fooddelivery.entity.DeliveryDriver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Long> {
    List<DeliveryDriver> findByAvailable(boolean available);
}
