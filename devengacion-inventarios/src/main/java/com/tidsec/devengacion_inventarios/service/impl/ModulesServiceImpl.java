package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tidsec.devengacion_inventarios.entity.Modules;
import com.tidsec.devengacion_inventarios.repository.IModulesRepository;
import com.tidsec.devengacion_inventarios.service.IModulesService;


@Service
public class ModulesServiceImpl implements IModulesService {
	@Autowired
	private IModulesRepository modulesRepository;

	@Override
	public List<Modules> getModules() {
		return modulesRepository.findByStatusNot(0);
	}

	@Override
	public Modules getForId(Integer id) {
		return modulesRepository.findById(id).orElse(null);
	}

	@Override
	public Modules createModule(Modules module) {
		return modulesRepository.save(module);
	}

	@Override
	public Modules updateModule(Integer id, Modules module) {
		Modules moduledb = modulesRepository.findById(id).orElse(null);
		if (moduledb != null) {
			moduledb.setName(module.getName());
			moduledb.setDescription(module.getDescription());
			moduledb.setStatus(module.getStatus());

			return modulesRepository.save(moduledb);
		} else {
			return null;
		}
	}

	@Override
	public boolean deleteModule(Integer id) {
		Modules moduledb = modulesRepository.findById(id).orElse(null);
		if (moduledb != null) {
			moduledb.setStatus(0);
			modulesRepository.save(moduledb);
			return true;
		} else {
			return false;
		}
	}
	

}
