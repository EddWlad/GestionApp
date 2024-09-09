package com.tidsec.devengacion_inventarios.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tidsec.devengacion_inventarios.entity.Users;
import com.tidsec.devengacion_inventarios.repository.IUsersRepository;

public class UserDetailServiceImpl implements UserDetailsService{
@Autowired
	private IUsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Users userEntity = usersRepository.findByEmail(username)
	            .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

	    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
	    boolean active = false;
	    if (userEntity.getStatus() == 1 ) {
	        active = true;
	    }

	    return new User(userEntity.getEmail(), userEntity.getPassword(), active, true, true, true,
	            authorityList);
	}
}
