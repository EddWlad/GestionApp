package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Material;


@Repository
public interface IMaterialRepository extends JpaRepository<Material, Long> {
	List<Material> findByEstadoNot(Integer estado);
	Material findByCodigo(String codigo);
	Optional<Material> findByNombre(String nombre);
}
