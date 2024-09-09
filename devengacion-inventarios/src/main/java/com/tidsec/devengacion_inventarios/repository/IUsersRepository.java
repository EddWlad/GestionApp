package com.tidsec.devengacion_inventarios.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tidsec.devengacion_inventarios.entity.Users;

@Repository
public interface IUsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail( String email);
	List<Users> findAllByStatusNot(Integer status);
}
