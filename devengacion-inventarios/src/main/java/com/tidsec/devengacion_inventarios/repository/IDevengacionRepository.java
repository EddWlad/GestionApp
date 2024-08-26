package com.tidsec.devengacion_inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Devengacion;

@Repository
public interface IDevengacionRepository extends JpaRepository<Devengacion, Long> {
	List<Devengacion> findByEstadoNot(Integer estado);

}
