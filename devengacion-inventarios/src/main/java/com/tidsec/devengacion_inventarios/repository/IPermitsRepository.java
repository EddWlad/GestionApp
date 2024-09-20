package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Permits;

@Repository
public interface IPermitsRepository extends JpaRepository<Permits, Integer>{
    Optional<Permits> findByRole_idAndModules_id(Integer role_id, Integer modules_id);
	List<Permits> findByRole_id(Integer roleId);
	void deleteByRole_id(Integer roleId);
}
