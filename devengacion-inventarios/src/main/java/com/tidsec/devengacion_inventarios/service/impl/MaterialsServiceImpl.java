package com.tidsec.devengacion_inventarios.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tidsec.devengacion_inventarios.entity.Materials;
import com.tidsec.devengacion_inventarios.repository.IMaterialsRepository;
import com.tidsec.devengacion_inventarios.service.IMaterialsService;

@Service
public class MaterialsServiceImpl implements IMaterialsService{

	@Autowired
	private IMaterialsRepository materialsRepository;
	
	@Override
	public List<Materials> getAll() {
		return materialsRepository.findByStateNot(0);
	}

	@Override
	public Materials getById(Integer id) {
		return materialsRepository.findById(id).orElse(null);
	}

	@Override
	public Optional<Materials> findById(Integer id) {
		return materialsRepository.findById(id);
	}

	@Override
	public Materials saveMaterials(Materials materials) {
		return materialsRepository.save(materials);
	}

	@Override
	public Materials updateMaterials(Integer id, Materials materials) {
		Materials materialsDb = materialsRepository.findById(id).orElse(null);
		if (materialsDb != null) {
			materialsDb.setCode(materials.getCode());
			materialsDb.setName(materials.getName());
			materialsDb.setType(materials.getType());
			materialsDb.setState(materials.getState());
			//materialsDb.setSeries(materials.getSeries());
			//materialsDb.setInventories(materials.getInventories());
			return materialsRepository.save(materialsDb);
		} else {
			return null;
		}
	}

	@Override
	public boolean deleteMaterials(Integer id) {
		Materials materialsDb = materialsRepository.findById(id).orElse(null);
		if (materialsDb != null) {
			materialsDb.setState(0);
			materialsRepository.save(materialsDb);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Long countMaterials() {
		return materialsRepository.count();
	}

	@Override
	public void uploadMaterialsFromExcel(MultipartFile file) {
	    try (InputStream inputStream = file.getInputStream()) {
	        Workbook workbook = new XSSFWorkbook(inputStream);
	        Sheet sheet = workbook.getSheetAt(0);

	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) {
	                continue;
	            }

	            int code = (int) row.getCell(0).getNumericCellValue();
	            String name = row.getCell(1).getStringCellValue();
	            String type = row.getCell(2).getStringCellValue();

	            Materials existingMaterialByCode = materialsRepository.findByCode(code);
	            Materials existingMaterialByName = materialsRepository.findByName(name);

	            if (existingMaterialByCode != null) {
	                existingMaterialByCode.setName(name);
	                existingMaterialByCode.setType(type);
	                existingMaterialByCode.setState(1);
	                materialsRepository.save(existingMaterialByCode);
	            } else if (existingMaterialByName != null) {
	                existingMaterialByName.setCode(code);
	                existingMaterialByName.setType(type);
	                existingMaterialByName.setState(1);
	                materialsRepository.save(existingMaterialByName);
	            } else {
	                Materials newMaterial = new Materials();
	                newMaterial.setCode(code);
	                newMaterial.setName(name);
	                newMaterial.setType(type);
	                newMaterial.setState(1);
	                materialsRepository.save(newMaterial);
	            }
	        }
	        workbook.close();
	    } catch (IOException e) {
	        throw new RuntimeException("Error al leer el archivo Excel", e);
	    }
	}

	@Override
	public Materials findByName(String name) {
		return materialsRepository.findByName(name);
	}
	
	@Override
	public Materials findByCode(int code) {
		return materialsRepository.findByCode(code);
	}

	@Override
	public Optional<Materials> findOptionalByName(String name) {
		return materialsRepository.findOptionalByName(name);
	}
}
