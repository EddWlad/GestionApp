package com.tidsec.devengacion_inventarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.tidsec.devengacion_inventarios.entity.Materials;

public interface IMaterialsService {
	List<Materials> getAll();
	Materials getById(Integer id);
	Optional<Materials> findById(Integer id);
	Materials saveMaterials(Materials materials);
	Materials updateMaterials(Integer id, Materials materials);
	boolean deleteMaterials(Integer id);
	Long countMaterials();
	
	void uploadMaterialsFromExcel(MultipartFile file);
	
	Materials findByName(String name);
	Materials findByCode(int code);
	Optional<Materials> findOptionalByName(String name);
}
