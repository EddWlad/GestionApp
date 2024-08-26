package com.tidsec.devengacion_inventarios.service;

import java.util.List;
import java.util.Optional;

import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.entity.MaterialActivo;

public interface IMaterialActivoService {
	List<MaterialActivo> obtenerTodos();
	MaterialActivo obtenerPorId(Long id);
	Optional<MaterialActivo> buscarPorId(Long id);
	MaterialActivo crearMaterialActivo(MaterialActivo materialActivo);
	MaterialActivo actualizarMaterialActivo(Long id, MaterialActivo materialActivo);
	boolean eliminarMaterialActivo(Long id);
	Long contarMaterialActivo();
	
    List<MaterialActivo> buscarPorInventario(Inventario inventario);
    void guardarMaterialActivo(MaterialActivo materialActivo);
}
