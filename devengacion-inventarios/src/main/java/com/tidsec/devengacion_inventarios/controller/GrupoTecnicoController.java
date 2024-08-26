package com.tidsec.devengacion_inventarios.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;
import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.service.IGrupoTecnicoService;
import com.tidsec.devengacion_inventarios.service.IInventarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/grupos")
public class GrupoTecnicoController {
	@Autowired
	private IGrupoTecnicoService service;
	@Autowired
	private IInventarioService inventarioService;

	// @Autowired
	// private IUsuariosService usuariosService;

	@GetMapping
	public String listarGruposTecnicos(Model model) {
		List<GrupoTecnico> gruposTecnicos = service.obtenerTodos();
		model.addAttribute("activoGrupoTecnicos", "active");
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		// String nombreUsuario = authentication.getName();
		// Usuarios usuario = usuariosService.findByCorreo(nombreUsuario);
		// model.addAttribute("activoEmpresa", "active");
		// model.addAttribute("nombreRol", usuario.getRoles().getNombre());
		// model.addAttribute("nombreUsuario", usuario.getNombre() + " " +
		// usuario.getApellido());
		model.addAttribute("listarGrupoTecnico", gruposTecnicos);
		model.addAttribute("grupoTecnico", new GrupoTecnico());
		return "grupos";
	}

	@PostMapping("/guardar")
	public String guardarGrupoTecnico(@Valid @ModelAttribute GrupoTecnico grupoTecnico, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				GrupoTecnico codigoValido = service.buscarPorCodigo(grupoTecnico.getCodigo());
				if (codigoValido == null) {
					GrupoTecnico grupoTecnicoGuardar = service.crearGrupoTecnico(grupoTecnico);
					List<Inventario> inventarios = inventarioService.buscarPorGrupoTecnico(grupoTecnicoGuardar);
					grupoTecnicoGuardar.setInventarios(inventarios);
					service.crearGrupoTecnico(grupoTecnico);
					redirectAttributes.addFlashAttribute("message", "El grupo ha sido guardada con éxito");
				} else if (codigoValido != null && codigoValido.getEstado() == 0) {
					grupoTecnico.setEstado(1);
					service.actualizarGrupoTecnico(codigoValido.getId(), grupoTecnico);
					redirectAttributes.addFlashAttribute("message", "El grupo ha sido guardado con éxito.");
				} else {
					redirectAttributes.addFlashAttribute("message", "El grupo ya existe.");
				}

			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/grupos";
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/grupos";
		}
	}

	@GetMapping("/editar/{id}")
	@ResponseBody
	public GrupoTecnico formularioEditarGrupoTecnico(@PathVariable Long id) {
		GrupoTecnico grupoTecnico = service.obtenerPorId(id);
		return grupoTecnico;
	}

	@PutMapping("editar/{id}")
	public String editarGrupoTecnico(@PathVariable Long id, @Valid @ModelAttribute GrupoTecnico grupoTecnico,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (!bindingResult.hasErrors()) {
			try {
				GrupoTecnico grupoTecnicoActual = service.obtenerPorId(id);
				if (grupoTecnicoActual != null) {
					grupoTecnico.setInventarios(grupoTecnicoActual.getInventarios());
					service.actualizarGrupoTecnico(id, grupoTecnico);
					redirectAttributes.addFlashAttribute("message", "El grupo ha sido modificado con éxito");
				} else {
					redirectAttributes.addFlashAttribute("message", "El grupo no existe.");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
		}
		return "redirect:/grupos";
	}

	@DeleteMapping("/eliminar/{id}")
	@ResponseBody
		public boolean mensajeEliminarGrupoTecnico(@PathVariable Long id) {
			boolean result = service.eliminarGrupoTecnico(id);
			if (result) {
				return true;
			} else {
				return false;
			}
		}

}
