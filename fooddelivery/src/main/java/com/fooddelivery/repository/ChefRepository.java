package com.fooddelivery.repository;

import com.fooddelivery.entity.Chef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChefRepository extends JpaRepository<Chef, Long> {
    List<Chef> findByAvailable(boolean available);
}
