package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tidsec.devengacion_inventarios.entity.TechnicalGroup;
import com.tidsec.devengacion_inventarios.repository.ITechnicalGroupRepository;
import com.tidsec.devengacion_inventarios.service.ITechnicalGroupService;

@Service
public class TechnicalGroupServiceImpl implements ITechnicalGroupService {
	
	@Autowired
	private ITechnicalGroupRepository technicalGroupRepository;

	@Override
	public List<TechnicalGroup> getAll() {
		return technicalGroupRepository.findByStateNot(0);
	}

	@Override
	public TechnicalGroup getById(Integer id) {
		return technicalGroupRepository.findById(id).orElse(null);
	}

	@Override
	public Optional<TechnicalGroup> findById(Integer id) {
		return technicalGroupRepository.findById(id);
	}

	@Override
	public TechnicalGroup saveTechnicalGroup(TechnicalGroup technicalGroup) {
		return technicalGroupRepository.save(technicalGroup);
	}

	@Override
	public TechnicalGroup updateTechnicalGroup(Integer id, TechnicalGroup technicalGroup) {
		TechnicalGroup technicalGroupDb = technicalGroupRepository.findById(id).orElse(null);
		if (technicalGroupDb  != null) {
			technicalGroupDb.setName(technicalGroup.getName());
			technicalGroupDb.setDescription(technicalGroup.getDescription());
			technicalGroupDb.setGroupLeader(technicalGroup.getGroupLeader());
			technicalGroupDb.setState(technicalGroup.getState());
			//technicalGroupDb.setInventory(technicalGroup.getInventory());
			return technicalGroupRepository.save(technicalGroupDb);
		}else {
			return null;
		}
	}

	@Override
	public boolean deleteTechnicalGroup(Integer id) {
		TechnicalGroup technicalGroupDb = technicalGroupRepository.findById(id).orElse(null);
		if(technicalGroupDb != null)
		{
			technicalGroupDb.setState(0);
			technicalGroupRepository.save(technicalGroupDb);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public Long countTechnicalGroup() {
		return technicalGroupRepository.count();
	}

	@Override
	public TechnicalGroup findByName(String name) {
		return technicalGroupRepository.findByName(name);
	}

	@Override
	public Optional<TechnicalGroup> findOptionalByName(String name) {
		return technicalGroupRepository.findOptionalByName(name);
	}

}
