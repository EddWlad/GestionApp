package com.tidsec.devengacion_inventarios.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tidsec.devengacion_inventarios.entity.Modules;
import com.tidsec.devengacion_inventarios.entity.Permits;
import com.tidsec.devengacion_inventarios.entity.Role;
import com.tidsec.devengacion_inventarios.entity.Users;
import com.tidsec.devengacion_inventarios.models.ModulesPermitsDto;
import com.tidsec.devengacion_inventarios.models.PermitDTO;
import com.tidsec.devengacion_inventarios.models.PermitsDTO;
import com.tidsec.devengacion_inventarios.models.RolesPermitsDTO;
import com.tidsec.devengacion_inventarios.service.IModulesService;
import com.tidsec.devengacion_inventarios.service.IPermitsService;
import com.tidsec.devengacion_inventarios.service.IRolesService;
import com.tidsec.devengacion_inventarios.service.IUsersService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/roles")
public class RolesController {
	@Autowired
	private IRolesService rolesService;
	@Autowired
	private IModulesService moduloService;
	@Autowired
	private IPermitsService permitsService;
	@Autowired
	private IUsersService usersService;

	@GetMapping
	public String listRoles(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String nameUser = authentication.getName();
		Users user = usersService.findByEmail(nameUser);
		List<Role> roles = rolesService.getRoles();
		model.addAttribute("activoUsuarios", "active");
		model.addAttribute("nombreRol", user.getRole().getName());
		model.addAttribute("nombreUsuario", user.getName() + " " + user.getLastName());
		model.addAttribute("roles", roles);
		return "roles";
	}

	@PostMapping("/save")
	public String saverRole(@Valid @ModelAttribute Role role, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				rolesService.createRol(role);
				redirectAttributes.addFlashAttribute("message", "El rol ha sido guardado con éxito");
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/roles";
		} else {
			redirectAttributes.addFlashAttribute("message",bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/roles";
		}
	}

	@GetMapping("/update/{id}")
	@ResponseBody
	public Role showEditRoleForm(@PathVariable Integer id) {
		Role role = rolesService.getRole(id);
		return role;
	}

	@PutMapping("update/{id}")
	public String updateRole(@PathVariable Integer id, @Valid @ModelAttribute Role role, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				rolesService.updateRol(id, role);
				redirectAttributes.addFlashAttribute("message", "El rol ha sido modificada con éxito");
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/roles";
		} else {
			redirectAttributes.addFlashAttribute("message",bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/roles";
		}
	}

	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public boolean deleteRol(@PathVariable Integer id) {
		return rolesService.deleteRol(id);
	}

	@GetMapping("/permits/{id}")
	@ResponseBody
	public RolesPermitsDTO obtenerPermisos(@PathVariable Integer id) {

		List<Modules> arrModules = moduloService.getModules();
		List<Permits> permits = permitsService.getPermitsForIdRole(id);
		Role role = rolesService.getRole(id);

		Map<Integer, List<Permits>> permitsByModule = permits.stream().collect(Collectors.groupingBy(permit -> {
			return permit.getModules() != null ? permit.getModules().getId() : 0;
		}));

		List<ModulesPermitsDto> modulePermissions = arrModules.stream().map(module -> {
			List<Permits> modulePermits = new ArrayList<>();
			if (permitsByModule.containsKey(module.getId())) {
				modulePermits = permitsByModule.get(module.getId());
			} else {
				Permits permit = new Permits();
				permit.setRead(false);
				permit.setUpdate(false);
				permit.setWrite(false);
				permit.setDelete(false);
				modulePermits.add(permit);
			}

			return new ModulesPermitsDto(module.getId(), module.getName(), modulePermits.get(0));
		}).collect(Collectors.toList());

		return new RolesPermitsDTO(role.getId(), role.getName(), modulePermissions);
	}

	@PostMapping(path = "/permits/{id}", consumes = "application/json")
	public String actualizarPermisos(@PathVariable Integer id,@Valid @RequestBody PermitsDTO permit,
			RedirectAttributes redirectAttributes) {

			try {
				boolean resultDelete = permitsService.deletePermitForRole(id);
				if (resultDelete) {
					List<PermitDTO> permisosDto = permit.getPermits();
					Role role = rolesService.getRole(id);
					permisosDto.forEach(p -> {
						Modules modulo = moduloService.getForId(p.getModules_id());
						Permits permits = new Permits();
						permits.setModules(modulo);
						permits.setRole(role);
						permits.setRead(p.isRead());
						permits.setWrite(p.isWrite());
						permits.setUpdate(p.isUpdate());
						permits.setDelete(p.isDelete());
						permitsService.createPermit(permits);
					});
					redirectAttributes.addFlashAttribute("message", "Los permisos se guardaron con éxito");
				} else {
					redirectAttributes.addFlashAttribute("message", "Error al crear los permisos");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
		return "redirect:/roles";

	}

}
