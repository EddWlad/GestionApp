package com.tidsec.devengacion_inventarios.service;

import java.util.List;

import com.tidsec.devengacion_inventarios.entity.Permits;

public interface IPermitsService {
    Permits getForRoles(Integer role_id, Integer modulos_id);
	Permits getForId(Integer id);
	Permits createPermit(Permits permit);
	boolean deletePermitForRole(Integer role_id);
	void deletePermit(Integer id);
	List<Permits> getPermitsForIdRole(Integer id);
	boolean checkPermission(String requestedUrl, String method, String userName);
}
