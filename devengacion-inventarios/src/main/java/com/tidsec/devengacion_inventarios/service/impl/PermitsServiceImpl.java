package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tidsec.devengacion_inventarios.entity.Modules;
import com.tidsec.devengacion_inventarios.entity.Permits;
import com.tidsec.devengacion_inventarios.entity.Users;
import com.tidsec.devengacion_inventarios.repository.IModulesRepository;
import com.tidsec.devengacion_inventarios.repository.IPermitsRepository;
import com.tidsec.devengacion_inventarios.repository.IUsersRepository;
import com.tidsec.devengacion_inventarios.service.IPermitsService;

@Service
public class PermitsServiceImpl implements IPermitsService{
	@Autowired
	private IPermitsRepository permitsRepository;

	@Autowired
	private IUsersRepository usersRepository;

	@Autowired
	private IModulesRepository modulesRepository;
    @Override
    public Permits getForRoles(Integer role_id, Integer modules_id) {
		return permitsRepository.findByRole_idAndModules_id(role_id, modules_id).orElse(null);
    }

    @Override
    public Permits getForId(Integer id) {
		return permitsRepository.findById(id).orElse(null);
    }

    @Override
    public Permits createPermit(Permits permit) {
        return permitsRepository.save(permit);
    }

    @Override
    public boolean deletePermitForRole(Integer role_id) {
        try {
			permitsRepository.findByRole_id(role_id);
			return true;
		}catch(Exception e) {
			return false;
		}
    }

    @Override
    public void deletePermit(Integer id) {
        permitsRepository.deleteById(id);
    }

    @Override
    public List<Permits> getPermitsForIdRole(Integer id) {
        return permitsRepository.findByRole_id(id);
    }

    @Override
    public boolean checkPermission(String requestedUrl, String method, String userName) {
        Users user = usersRepository.findByEmail(userName).orElse(null);
		if(user != null) {
			
			String nameModulo =requestedUrl.split("/")[1];
			String resultNameModulo = nameModulo.substring(0, 1) + nameModulo.substring(1);
			Modules module = modulesRepository.findByEndpointIgnoreCase(resultNameModulo.trim());
			if(module!=null) {
				Permits permits = getForRoles(user.getRole().getId(), module.getId());
				boolean result= false;
				if(permits!=null) {
					switch(method.toLowerCase()) {
					case "get":
						result = permits.isRead();
						break;
					case "post":
						result = permits.isWrite();
						break;
					case "delete":
						result = permits.isDelete();
						break;
					case "put":
						result = permits.isUpdate();
						break;
					default:
						break;
					}
					return result;
				}else {
					return false;
				}
				
			}else {
				return false;
			}
		}else {
			return false;
		}
    }

}
