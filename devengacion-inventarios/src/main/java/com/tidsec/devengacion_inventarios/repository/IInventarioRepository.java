package com.tidsec.devengacion_inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;
import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.entity.Material;


@Repository
public interface IInventarioRepository extends JpaRepository<Inventario, Long> {
	List<Inventario> findByEstadoNot(Integer estado);
	List<Inventario> findByGrupoTecnico(GrupoTecnico grupoTecnico);
	List<Inventario> findByMaterial(Material material);
	
    List<Inventario> findByGrupoTecnicoId(Long grupoTecnicoId);
    List<Inventario> findByMaterialCodigo(String codigo);
    Inventario findByGrupoTecnicoIdAndMaterialId(Long grupoTecnicoId, Long materialId);
}
