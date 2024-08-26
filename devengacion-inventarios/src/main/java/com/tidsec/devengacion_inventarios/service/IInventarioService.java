package com.tidsec.devengacion_inventarios.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.springframework.web.multipart.MultipartFile;

import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;
import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.entity.Material;
import com.tidsec.devengacion_inventarios.entity.MaterialActivo;


public interface IInventarioService {
	List<Inventario> obtenerTodos();
	Inventario obtenerPorId(Long id);
	Optional<Inventario> buscarPorId(Long id);
	Inventario crearInventario(Inventario inventario);
	Inventario actualizarInventario(Long id, Inventario inventario);
	boolean eliminarInventario(Long id);
	Long contarInventario();
	
	List<Inventario> buscarPorGrupoTecnico(GrupoTecnico grupoTecnico);
	List<Inventario> buscarPorMaterial(Material material);
	
	List<Inventario> cargarInventariosDesdeExcel(MultipartFile file) throws IOException;
	String getCellValue(Cell cell);
	void guardarInventarios(List<Inventario> inventarios);
	
	void guardarMaterialActivo(MaterialActivo materialActivo);
	
    List<Inventario> obtenerMaterialesPorGrupo(Long grupoTecnicoId);
    void eliminarPorSerie(String codigoMaterial);
    void decrementarCantidad(Long grupoTecnicoId, Long materialId, Integer cantidad);
}
