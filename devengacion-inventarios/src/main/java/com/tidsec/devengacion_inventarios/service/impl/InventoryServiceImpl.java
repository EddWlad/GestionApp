package com.tidsec.devengacion_inventarios.service.impl;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tidsec.devengacion_inventarios.entity.Inventory;
import com.tidsec.devengacion_inventarios.entity.Materials;
import com.tidsec.devengacion_inventarios.entity.Serial;
import com.tidsec.devengacion_inventarios.entity.TechnicalGroup;
import com.tidsec.devengacion_inventarios.repository.IInventoryRepository;
import com.tidsec.devengacion_inventarios.repository.IMaterialsRepository;
import com.tidsec.devengacion_inventarios.repository.ITechnicalGroupRepository;
import com.tidsec.devengacion_inventarios.service.IInventoryService;

public class InventoryServiceImpl implements IInventoryService {
	
    @Autowired
    private IInventoryRepository inventoryRepository;
    
    @Autowired
    private ITechnicalGroupRepository technicalGroupRepository;
    
    @Autowired
    private IMaterialsRepository materialsRepository;
    

	@Override
	public List<Inventory> getAllActiveInventories() {
		return inventoryRepository.findByStateNot(0);
	}

	@Override
	public Optional<Inventory> getInventoryById(Integer id) {
        return inventoryRepository.findById(id);
	}

	@Override
	public Inventory saveInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
	}
	
	@Override
	public Optional<Inventory> getInventoryByTechnicalGroupId(Integer technicalGroupId) {
        return inventoryRepository.findByTechnicalGroupId(technicalGroupId);
	}

	@Override
	public Inventory updateInventory(Integer id, Inventory updatedInventory) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (inventoryOpt.isPresent()) {
            Inventory existingInventory = inventoryOpt.get();
            existingInventory.setMaterials(updatedInventory.getMaterials());
            existingInventory.setState(updatedInventory.getState());
            return inventoryRepository.save(existingInventory);
        }
        return null;
	}

	@Override
	public boolean deleteInventory(Integer id) {
        Optional<Inventory> inventoryOpt = inventoryRepository.findById(id);
        if (inventoryOpt.isPresent()) {
            Inventory inventory = inventoryOpt.get();
            inventory.setState(0);
            inventoryRepository.save(inventory);
            return true;
        }
        return false;
		
	}

	@Override
	public void loadMaterialsFromExcel(MultipartFile file) {
	    try {
	        InputStream inputStream = file.getInputStream();
	        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
				Sheet sheet = workbook.getSheetAt(0);

				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				    Row row = sheet.getRow(i);
				    if (row != null) {
				        String groupCode = row.getCell(0).getStringCellValue();
				        Integer materialCode = (int) row.getCell(1).getNumericCellValue();
				        String serialNumber1 = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : null;
				        String serialNumber2 = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : null;
				        int quantity = (int) row.getCell(5).getNumericCellValue();

				        TechnicalGroup technicalGroup = technicalGroupRepository.findByName(groupCode);
				        if (technicalGroup == null) {
				            throw new RuntimeException("El grupo técnico con el código " + groupCode + " no existe.");
				        }

				        Materials material = materialsRepository.findByCode(materialCode);
				        if (material == null) {
				            throw new RuntimeException("El material con el código " + materialCode + " no existe.");
				        }

				        Optional<Inventory> optionalInventory = getInventoryByTechnicalGroupId(technicalGroup.getId());
				        Inventory inventory = optionalInventory.orElseGet(() -> new Inventory());


				        if (inventory.getId() == null ) {  
				            inventory.setTechnicalGroup(technicalGroup);
				            inventory.setDateDownload(new Date());
				            inventory.setState(1); 
				            inventory.setAmount(0);
				            inventoryRepository.save(inventory);
				        }

				        if (serialNumber1 != null) {
				            Serial newSerial = new Serial();
				            newSerial.setSerialNumber1(serialNumber1);
				            if (serialNumber2 != null) {
				                newSerial.setSerialNumber2(serialNumber2);
				            }
				            newSerial.setStatus("Activo");
				            newSerial.setInventory(inventory);
				            newSerial.setState(1);
				            inventory.getSeries().add(newSerial);
				        } else {
				            inventory.setAmount(inventory.getAmount() + quantity);
				        }

				        inventoryRepository.save(inventory);
				    }
				}
				workbook.close();
			}
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
