package com.tidsec.devengacion_inventarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.tidsec.devengacion_inventarios.entity.Inventory;


public interface IInventoryService {
    List<Inventory> getAllActiveInventories();
    Optional<Inventory> getInventoryById(Integer id);
    Inventory saveInventory(Inventory inventory);
    Inventory updateInventory(Integer id, Inventory updatedInventory);
    boolean deleteInventory(Integer id);
    void loadMaterialsFromExcel(MultipartFile file);
    Optional<Inventory> getInventoryByTechnicalGroupId(Integer technicalGroupId);
 
}
