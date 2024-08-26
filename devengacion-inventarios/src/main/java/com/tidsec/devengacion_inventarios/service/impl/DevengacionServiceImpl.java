package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tidsec.devengacion_inventarios.entity.Devengacion;
import com.tidsec.devengacion_inventarios.repository.IDevengacionRepository;
import com.tidsec.devengacion_inventarios.service.IDevengacionService;

@Service
public class DevengacionServiceImpl implements IDevengacionService {

	@Autowired
	private IDevengacionRepository devengacionRepository;
	
	@Override
	public List<Devengacion> obtenerTodas() {
		return devengacionRepository.findByEstadoNot(0);
	}

	@Override
	public Devengacion obtenerPorId(Long id) {
		return devengacionRepository.findById(id).orElse(null);
	}

	@Override
	public Optional<Devengacion> buscarPorId(Long id) {
		return devengacionRepository.findById(id);
	}

	@Override
	public Devengacion crearDevengacion(Devengacion devengacion) {
		return devengacionRepository.save(devengacion);
	}

	@Override
	public Devengacion actualizarDevengacion(Long id, Devengacion devengacion) {
        Optional<Devengacion> optionalDevengacion = devengacionRepository.findById(id);
        if (optionalDevengacion.isPresent()) {
            Devengacion devengacionExistente = optionalDevengacion.get();
            devengacionExistente.setProyectoId(devengacion.getProyectoId());
            devengacionExistente.setCluster(devengacion.getCluster());
            devengacionExistente.setTipoInstalacion(devengacion.getTipoInstalacion());
            devengacionExistente.setInventario(devengacion.getInventario());
            //devengacionExistente.setCantidad(devengacion.getCantidad());
            devengacionExistente.setFechaDevengacion(devengacion.getFechaDevengacion());
            devengacionExistente.setFechaInstalacion(devengacion.getFechaInstalacion());
            return devengacionRepository.save(devengacionExistente);
        } else {
            return null;
        }
	}

	@Override
	public boolean eliminarDevengacion(Long id) {
		Devengacion devengacionDb = devengacionRepository.findById(id).orElse(null);
		if(devengacionDb != null)
		{
			devengacionDb.setEstado(0);
			devengacionRepository.save(devengacionDb);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Long contarDevengaciones() {
		return devengacionRepository.count();
	}

}
