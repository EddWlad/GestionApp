package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Inventory;

@Repository
public interface IInventoryRepository extends JpaRepository<Inventory, Integer>  {

    List<Inventory> findByStateNot(Integer state);
    
    Optional<Inventory> findByTechnicalGroupId(Integer technicalGroupId);
}
