package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.TechnicalGroup;

@Repository
public interface ITechnicalGroupRepository extends JpaRepository<TechnicalGroup, Integer> {
	List<TechnicalGroup> findByStateNot(Integer state);
	TechnicalGroup findByName(String name);
    Optional<TechnicalGroup> findOptionalByName(String name);
}
