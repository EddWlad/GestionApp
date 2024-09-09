package com.tidsec.devengacion_inventarios.service;

import java.util.List;

import com.tidsec.devengacion_inventarios.entity.Role;

public interface IRolesService {
    List<Role> getRoles();
	Role getRole(Integer id);
	Role createRol(Role role);
	Role updateRol(Integer id, Role role);
	boolean deleteRol(Integer id);
}
