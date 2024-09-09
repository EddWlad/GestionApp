package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Role;

@Repository
public interface IRolesRepository extends JpaRepository<Role, Integer>{
    Optional<Role> findByName(String name);
	List<Role> findAllByStatusNot(Integer status);
}
