package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tidsec.devengacion_inventarios.entity.Role;
import com.tidsec.devengacion_inventarios.repository.IRolesRepository;
import com.tidsec.devengacion_inventarios.service.IRolesService;

public class RolesServiceImpl implements IRolesService{

    @Autowired
	private IRolesRepository rolesRepository;

    @Override
    public List<Role> getRoles() {
        return rolesRepository.findAllByStatusNot(0);
    }

    @Override
    public Role getRole(Integer id) {
        return rolesRepository.findById(id).orElse(null);
    }

    @Override
    public Role createRol(Role role) {
        return rolesRepository.save(role);
    }

    @Override
    public Role updateRol(Integer id, Role role) {
        Role roleDb = rolesRepository.findById(id).orElse(null);
		if (roleDb != null) {
			roleDb.setName(role.getName());
			roleDb.setDescription(role.getDescription());
			roleDb.setStatus(role.getStatus());

			return rolesRepository.save(roleDb);
		} else {
			return null;
		}
    }

    @Override
    public boolean deleteRol(Integer id) {
        Role roleDb = rolesRepository.findById(id).orElse(null);
		if (roleDb != null && roleDb.getName() != "Administrador") {
			roleDb.setStatus(0);
			rolesRepository.save(roleDb);
			return true;
		} else {
			return false;
		}
    }

}
