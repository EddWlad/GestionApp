package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;


@Repository
public interface IGrupoTecnicoRepository extends JpaRepository<GrupoTecnico, Long> {

	List<GrupoTecnico> findByEstadoNot(Integer estado);
	GrupoTecnico findByCodigo(String codigo);
    Optional<GrupoTecnico> findOptionalByCodigo(String codigo);
	//Optional<GrupoTecnico> findByNombre(String nombre);
}
