package com.fooddelivery.service;

import com.fooddelivery.entity.DeliveryDriver;
import com.fooddelivery.repository.DeliveryDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryDriverService {
    @Autowired
    private DeliveryDriverRepository deliveryDriverRepository;
    public List<DeliveryDriver> getAvailableDrivers(){
        return deliveryDriverRepository.findByAvailable(true);
    }

    public DeliveryDriver assignDriverToOrder(Long driverId, Long OrderId){
        Optional<DeliveryDriver> driverOpt = deliveryDriverRepository.findById(driverId);
        if(driverOpt.isPresent()){
            DeliveryDriver driver = driverOpt.get();
            driver.setAvailable(false);
            return deliveryDriverRepository.save(driver);
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
