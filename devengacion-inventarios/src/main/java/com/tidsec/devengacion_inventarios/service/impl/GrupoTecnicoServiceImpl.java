package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;
import com.tidsec.devengacion_inventarios.repository.IGrupoTecnicoRepository;
import com.tidsec.devengacion_inventarios.service.IGrupoTecnicoService;

@Service
public class GrupoTecnicoServiceImpl implements IGrupoTecnicoService {

	@Autowired
	private IGrupoTecnicoRepository grupoTecnicoRepository;
	
	@Override
	public List<GrupoTecnico> obtenerTodos() {
		return grupoTecnicoRepository.findByEstadoNot(0);
	}

	@Override
	public GrupoTecnico obtenerPorId(Long id) {
		return grupoTecnicoRepository.findById(id).orElse(null);
	}

	@Override
	public Optional<GrupoTecnico> buscarPorId(Long id) {
		return grupoTecnicoRepository.findById(id);
	}

	@Override
	public GrupoTecnico crearGrupoTecnico(GrupoTecnico grupoTecnico) {
		return grupoTecnicoRepository.save(grupoTecnico);
	}

	@Override
	public GrupoTecnico actualizarGrupoTecnico(Long id, GrupoTecnico grupoTecnico) {
		GrupoTecnico grupoTecnicoDb = grupoTecnicoRepository.findById(id).orElse(null);
		if (grupoTecnicoDb  != null) {
			grupoTecnicoDb.setCodigo(grupoTecnico.getCodigo());
			grupoTecnicoDb.setJefeDeGrupo(grupoTecnico.getJefeDeGrupo());
			grupoTecnicoDb.setDescripcion(grupoTecnico.getDescripcion());
			grupoTecnicoDb.setEstado(grupoTecnico.getEstado());
			return grupoTecnicoRepository.save(grupoTecnicoDb);
		}else {
			return null;
		}
	}

	@Override
	public boolean eliminarGrupoTecnico(Long id) {
		GrupoTecnico grupoTecnicoDb = grupoTecnicoRepository.findById(id).orElse(null);
		if(grupoTecnicoDb != null)
		{
			grupoTecnicoDb.setEstado(0);
			grupoTecnicoRepository.save(grupoTecnicoDb);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Long contarGrupoTecnico() {
		return grupoTecnicoRepository.count();
	}

	@Override
	public GrupoTecnico buscarPorCodigo(String codigo) {
		return grupoTecnicoRepository.findByCodigo(codigo);
	}

    public Optional<GrupoTecnico> buscarOptionalPorCodigo(String codigo) {
        return grupoTecnicoRepository.findOptionalByCodigo(codigo);
    }


	/*@Override
	public Optional<GrupoTecnico> buscarPorNombre(String nombre) {
		return grupoTecnicoRepository.findByNombre(nombre);
	}*/

}
