package com.tidsec.devengacion_inventarios.service.impl;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tidsec.devengacion_inventarios.entity.Material;
import com.tidsec.devengacion_inventarios.repository.IMaterialRepository;
import com.tidsec.devengacion_inventarios.service.IMaterialService;


@Service
public class MaterialServiceImpl implements IMaterialService {

	@Autowired
	private IMaterialRepository materialRepository;
	
    
	@Override
	public List<Material> cargarMaterialesDesdeExcel(MultipartFile file) throws IOException {
	    List<Material> materiales = new ArrayList<>();
	    InputStream inputStream = file.getInputStream();
	    Workbook workbook = new XSSFWorkbook(inputStream);
	    Sheet sheet = workbook.getSheetAt(0);
	    for (Row row : sheet) {
	    	if (row.getRowNum() == 0) {
	            continue;
	        }
	        Material material = new Material();
	        material.setCodigo(getCellValue(row.getCell(0)));
	        material.setNombre(getCellValue(row.getCell(1)));
	        material.setTipo(getCellValue(row.getCell(2)));
	        material.setEstado(1); // Activo por defecto (cambiar si es necesario para el borrado lógico)


	        materiales.add(material);
	    }
	    workbook.close();
	    return materiales;
	}

	@Override
	public String getCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((int) cell.getNumericCellValue());
        } else {
            return "";
        }
	}

	@Override
	public void guardarMateriales(List<Material> materiales) {
	    for (Material material : materiales) {
	        materialRepository.save(material);
	    }
	}
	
	@Override
	public List<Material> obtenerTodos() {
		return materialRepository.findByEstadoNot(0);
	}

	@Override
	public Material obtenerPorId(Long id) {
		return materialRepository.findById(id).orElse(null);
	}

	@Override
	public Optional<Material> buscarPorId(Long id) {
		return materialRepository.findById(id);
	}

	@Override
	public Material crearMaterial(Material material) {
		return materialRepository.save(material);
	}

	@Override
	public Material actualizarMaterial(Long id, Material material) {
		Material materialDb = materialRepository.findById(id).orElse(null);
		if (materialDb  != null) {
			materialDb.setNombre(material.getNombre());
			materialDb.setTipo(material.getTipo());
			materialDb.setEstado(material.getEstado());
			materialDb.setCodigo(material.getCodigo());
			return materialRepository.save(materialDb);
		}else {
			return null;
		}
	}

	@Override
	public boolean eliminarMaterial(Long id) {
		Material materialDb = materialRepository.findById(id).orElse(null);
		if(materialDb != null)
		{
			materialDb.setEstado(0);
			materialRepository.save(materialDb);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Long contarMaterial() {
		return materialRepository.count();
	}

	@Override
	public Material buscarPorCodigo(String codigo) {
		return materialRepository.findByCodigo(codigo);
	}

	@Override
	public Optional<Material> buscarPorNombre(String nombre) {
		return materialRepository.findByNombre(nombre);
	}


}
