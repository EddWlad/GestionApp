package com.tidsec.devengacion_inventarios.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tidsec.devengacion_inventarios.entity.Modules;

public interface IModulesRepository extends JpaRepository<Modules, Integer>{
    List<Modules> findByStatusNot(Integer status);
	Modules findByName(String name);
}
