package com.tidsec.devengacion_inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.entity.MaterialActivo;

@Repository
public interface IMaterialActivoRepository extends JpaRepository<MaterialActivo, Long>{
    List<MaterialActivo> findByEstadoNot(Integer estado);
    List<MaterialActivo> findByInventario(Inventario inventario);
	
    //List<MaterialActivo> findByMaterial(Material material);
}
