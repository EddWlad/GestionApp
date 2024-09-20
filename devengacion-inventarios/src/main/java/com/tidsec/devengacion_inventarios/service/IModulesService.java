package com.tidsec.devengacion_inventarios.service;

import java.util.List;

import com.tidsec.devengacion_inventarios.entity.Modules;

public interface IModulesService {
	List<Modules> getModules();
	Modules getForId(Integer id);
	Modules createModule(Modules module);
	Modules updateModule(Integer id, Modules module);
	boolean deleteModule(Integer id);
}
