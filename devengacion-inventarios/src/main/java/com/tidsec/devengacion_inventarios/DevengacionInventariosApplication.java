package com.tidsec.devengacion_inventarios;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tidsec.devengacion_inventarios.entity.Role;
import com.tidsec.devengacion_inventarios.entity.Users;
import com.tidsec.devengacion_inventarios.repository.IRolesRepository;
import com.tidsec.devengacion_inventarios.repository.IUsersRepository;

@SpringBootApplication
public class DevengacionInventariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevengacionInventariosApplication.class, args);
	}
@Bean
	CommandLineRunner init(IUsersRepository usersRepository, IRolesRepository rolesRepository){
		return args -> {
	            // Create ROLES
				
				Role role = rolesRepository.findByName("Administrador").orElse(null);
				if(role == null) {
					Role roleAdmin = new Role();
					roleAdmin.setId(1);;
					roleAdmin.setName("Administrador");
					roleAdmin.setDescription("Administrador del sistema");
					roleAdmin.setStatus(1);
					rolesRepository.save(roleAdmin);
				}

	            // CREATE USERS 
				Role rolAdmin1 = rolesRepository.findByName("Administrador").orElse(null);
				Users user = usersRepository.findByEmail("greencode@gmail.com").orElse(null);
				if(user == null) {
					Users userAdmin = new Users();
					userAdmin.setName("Cristhian");
					userAdmin.setLastName("Alcivar");
					userAdmin.setEmail("greencode@gmail.com");
					userAdmin.setPhone("0999035344");
					userAdmin.setPassword(new BCryptPasswordEncoder().encode("Uisrael2024"));
					userAdmin.setRole(rolAdmin1);
					userAdmin.setStatus(1);					
					
					usersRepository.save(userAdmin);					
				}
		};
	}
}
