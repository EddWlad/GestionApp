package com.tidsec.devengacion_inventarios.service;

import java.util.List;
import java.util.Optional;

import com.tidsec.devengacion_inventarios.entity.TechnicalGroup;

public interface ITechnicalGroupService {
	List<TechnicalGroup> getAll();
	TechnicalGroup getById(Integer id);
	Optional<TechnicalGroup> findById(Integer id);
	TechnicalGroup saveTechnicalGroup(TechnicalGroup technicalGroup);
	TechnicalGroup updateTechnicalGroup(Integer id, TechnicalGroup technicalGroup);
	boolean deleteTechnicalGroup(Integer id);
	Long countTechnicalGroup();
	
	TechnicalGroup findByName(String name);
	Optional<TechnicalGroup> findOptionalByName(String name);
}
