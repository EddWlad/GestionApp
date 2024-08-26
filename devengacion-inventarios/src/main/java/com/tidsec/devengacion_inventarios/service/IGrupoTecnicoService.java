package com.tidsec.devengacion_inventarios.service;

import java.util.List;
import java.util.Optional;

import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;


public interface IGrupoTecnicoService {

	List<GrupoTecnico> obtenerTodos();
	GrupoTecnico obtenerPorId(Long id);
	Optional<GrupoTecnico> buscarPorId(Long id);
	GrupoTecnico crearGrupoTecnico(GrupoTecnico grupoTecnico);
	GrupoTecnico actualizarGrupoTecnico(Long id, GrupoTecnico grupoTecnico);
	boolean eliminarGrupoTecnico(Long id);
	Long contarGrupoTecnico();
	
	GrupoTecnico buscarPorCodigo(String codigo);
	Optional<GrupoTecnico> buscarOptionalPorCodigo(String codigo);

}
