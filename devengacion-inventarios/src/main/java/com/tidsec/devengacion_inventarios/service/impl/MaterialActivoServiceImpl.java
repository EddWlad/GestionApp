package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tidsec.devengacion_inventarios.entity.Inventario;
//import com.tidsec.devengacion_inventarios.entity.Material;
import com.tidsec.devengacion_inventarios.entity.MaterialActivo;
import com.tidsec.devengacion_inventarios.repository.IMaterialActivoRepository;
import com.tidsec.devengacion_inventarios.service.IMaterialActivoService;


@Service
public class MaterialActivoServiceImpl implements IMaterialActivoService {

    @Autowired
    private IMaterialActivoRepository materialActivoRepository;

    @Override
    public List<MaterialActivo> obtenerTodos() {
        return materialActivoRepository.findByEstadoNot(0);
    }

    @Override
    public MaterialActivo obtenerPorId(Long id) {
        return materialActivoRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<MaterialActivo> buscarPorId(Long id) {
        return materialActivoRepository.findById(id);
    }

    @Override
    public MaterialActivo crearMaterialActivo(MaterialActivo materialActivo) {
        return materialActivoRepository.save(materialActivo);
    }

    @Override
    public MaterialActivo actualizarMaterialActivo(Long id, MaterialActivo materialActivo) {
        MaterialActivo materialActivoDb = materialActivoRepository.findById(id).orElse(null);
        if (materialActivoDb != null) {
            materialActivoDb.setSerie1(materialActivo.getSerie1());
            materialActivoDb.setSerie2(materialActivo.getSerie2());
            materialActivoDb.setEstado(materialActivo.getEstado());
            return materialActivoRepository.save(materialActivoDb);
        } else {
            return null;
        }
    }

    @Override
    public boolean eliminarMaterialActivo(Long id) {
        MaterialActivo materialActivoDb = materialActivoRepository.findById(id).orElse(null);
        if (materialActivoDb != null) {
            materialActivoDb.setEstado(0);
            materialActivoRepository.save(materialActivoDb);
            return true;
        } else {
            return false;
        }
    }

	@Override
	public Long contarMaterialActivo() {
		return materialActivoRepository.count();
	}

	@Override
	public List<MaterialActivo> buscarPorInventario(Inventario inventario) {
        return materialActivoRepository.findByInventario(inventario);
	}

	@Override
	public void guardarMaterialActivo(MaterialActivo materialActivo) {
        materialActivoRepository.save(materialActivo);
		
	}



}
