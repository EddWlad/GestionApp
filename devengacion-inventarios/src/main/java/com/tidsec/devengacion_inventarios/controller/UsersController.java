package com.tidsec.devengacion_inventarios.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tidsec.devengacion_inventarios.entity.Role;
import com.tidsec.devengacion_inventarios.entity.Users;
import com.tidsec.devengacion_inventarios.service.IRolesService;
import com.tidsec.devengacion_inventarios.service.IUsersService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsersController {
	@Autowired
	private IUsersService usersService;
	@Autowired
	private IRolesService rolesService;

	@GetMapping
	public String listUsers(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nameUser = authentication.getName();
		Users user = usersService.findByEmail(nameUser);
		List<Users> users = usersService.getUsers();
		List<Role> roles = rolesService.getRoles();
		model.addAttribute("activoUsuarios", "active");
		model.addAttribute("nombreRol", user.getRole().getName());
		model.addAttribute("nombreUsuario", user.getName() + " " + user.getLastName());
		model.addAttribute("usuarios", users);
		model.addAttribute("roles", roles);
		return "users";
	}

	@PostMapping("/save")
	public String saveUser(@Valid @ModelAttribute Users user, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				Users emailValid = usersService.findByEmail(user.getEmail());
				if (emailValid == null) {
						usersService.createUser(user);
						redirectAttributes.addFlashAttribute("message", "El usuario ha sido guardado con éxito.");
				} else if (emailValid != null && emailValid.getStatus() == 0) {
					user.setStatus(1);
					usersService.updateUser(emailValid.getId(), user);
					redirectAttributes.addFlashAttribute("message", "El usuario ha sido guardado con éxito.");
				} else {
					redirectAttributes.addFlashAttribute("message", "El correo ya existe.");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/users";
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/update/{id}")
	@ResponseBody
	public Users showEditUserForm(@PathVariable Integer id) {
		Users user = usersService.getUser(id);
		return user;
	}

	@PutMapping("/update/{id}")
	public String updateUser(@PathVariable Integer id, @Valid @ModelAttribute Users user,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (!bindingResult.hasErrors()) {
			try {
				Users emailValid = usersService.findByEmail(user.getEmail());
				if (emailValid == null) {
					usersService.updateUser(id, user);
						redirectAttributes.addFlashAttribute("message", "El usuario ha sido modificada con éxito");
				} else {
					if (emailValid.getEmail().equals(user.getEmail())) {
						usersService.updateUser(id, user);
						redirectAttributes.addFlashAttribute("message", "El usuario ha sido modificada con éxito");
						
					} else {
						redirectAttributes.addFlashAttribute("message", "El correo ya existe.");
					}
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/users";
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/users";
		}
	}

	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public boolean deleteUser(@PathVariable Integer id) {
		boolean result = usersService.deleteUser(id);
		if (result) {
			return true;
		} else {
			return false;
		}
	}
}
