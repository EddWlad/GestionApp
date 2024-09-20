package com.tidsec.devengacion_inventarios.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tidsec.devengacion_inventarios.entity.Users;
import com.tidsec.devengacion_inventarios.repository.IUsersRepository;
import com.tidsec.devengacion_inventarios.service.IUsersService;

@Service
public class UsersServiceImpl implements IUsersService {

    @Autowired
	private IUsersRepository usersRepository;
    @Override
    public List<Users> getUsers() {
		return usersRepository.findAllByStatusNot(0);
    }

    @Override
    public Users getUser(Integer id) {
        return usersRepository.findById(id).orElse(null);    }

    @Override
    public Users createUser(Users user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		return usersRepository.save(user);
    }

    @Override
    public Users updateUser(Integer id, Users user) {
        Users userDb = usersRepository.findById(id).orElse(null);
		if (userDb != null) {
			userDb.setName(user.getName());
			userDb.setLastName(user.getLastName());
			userDb.setPhone(user.getPhone());
			userDb.setEmail(user.getEmail());
			userDb.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			userDb.setOperations(user.getOperations());
            userDb.setRole(user.getRole());
            userDb.setAccruals(user.getAccruals());
			userDb.setStatus(user.getStatus());

			return usersRepository.save(userDb);
		} else {
			return null;
		}
    }

    @Override
    public boolean deleteUser(Integer id) {
        Users userDb = usersRepository.findById(id).orElse(null);
		if (userDb != null) {
			userDb.setStatus(0);
			usersRepository.save(userDb);
			return true;
		} else {
			return false;
		}
    }
    @Override
    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

}
