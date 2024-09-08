package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Materials;

@Repository
public interface IMaterialsRepository extends JpaRepository<Materials, Integer>{
	List<Materials> findByStateNot(Integer state);
	List<Materials> findAll();
	Materials findByName(String name);
	Materials findByCode(int code);
    Optional<Materials> findOptionalByName(String name);
}
