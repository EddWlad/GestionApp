package com.tidsec.devengacion_inventarios.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Cell;


import com.tidsec.devengacion_inventarios.entity.Material;

public interface IMaterialService {
	List<Material> obtenerTodos();
	Material obtenerPorId(Long id);
	Optional<Material> buscarPorId(Long id);
	Material crearMaterial(Material material);
	Material actualizarMaterial(Long id, Material material);
	boolean eliminarMaterial(Long id);
	Long contarMaterial();
	
	Material buscarPorCodigo(String codigo);
	
	List<Material> cargarMaterialesDesdeExcel(MultipartFile file)throws IOException;
	String getCellValue(Cell cell);
	void guardarMateriales(List<Material> materiales);
	
	Optional<Material> buscarPorNombre(String nombre);
}
