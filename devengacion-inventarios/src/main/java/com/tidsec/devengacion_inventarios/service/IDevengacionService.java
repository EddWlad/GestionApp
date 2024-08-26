package com.tidsec.devengacion_inventarios.service;

import java.util.List;
import java.util.Optional;

import com.tidsec.devengacion_inventarios.entity.Devengacion;


public interface IDevengacionService {
	List<Devengacion> obtenerTodas();
	Devengacion obtenerPorId(Long id);
	Optional<Devengacion> buscarPorId(Long id);
	Devengacion crearDevengacion(Devengacion devengacion);
	Devengacion actualizarDevengacion(Long id, Devengacion devengacion);
	boolean eliminarDevengacion(Long id);
	Long contarDevengaciones();	
}
