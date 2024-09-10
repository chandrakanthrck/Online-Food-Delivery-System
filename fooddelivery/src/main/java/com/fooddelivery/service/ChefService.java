package com.fooddelivery.service;

import com.fooddelivery.entity.Chef;
import com.fooddelivery.repository.ChefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChefService {
    @Autowired
    private ChefRepository chefRepository;

    public List<Chef> getAvailableChefs(){
        return chefRepository.findByAvailable(true);
    }
    //assign a chef to the order
    public Chef assignChefToOrder(Long chefId, Long orderId){
        Optional<Chef> chefOpt = chefRepository.findById(chefId);
        if(chefOpt.isPresent()){
            Chef chef = chefOpt.get();
            chef.setAvailable(false);
            return chefRepository.save(chef);
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
