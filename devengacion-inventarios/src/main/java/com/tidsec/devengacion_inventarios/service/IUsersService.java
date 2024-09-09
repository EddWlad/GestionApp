package com.tidsec.devengacion_inventarios.service;

import java.util.List;

import com.tidsec.devengacion_inventarios.entity.Users;

public interface IUsersService {
    List<Users> getUsers();
	Users getUser(Integer id);
	Users createUser(Users user);
	Users updateUser(Integer id, Users user);
	boolean deleteUser(Integer id);
	Users findByEmail(String email);
}
