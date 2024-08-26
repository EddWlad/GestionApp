package com.tidsec.devengacion_inventarios.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.entity.Material;

import com.tidsec.devengacion_inventarios.service.IInventarioService;

import com.tidsec.devengacion_inventarios.service.IMaterialService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/materiales")
public class MaterialController {
	@Autowired
	private IMaterialService service;

	@Autowired
	private IInventarioService inventarioService;


	// @Autowired
	// private IUsuariosService usuariosService;

	@GetMapping
	public String listarMateriales(Model model) {
		List<Material> materiales = service.obtenerTodos();
		model.addAttribute("activoMateriales", "active");
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		// String nombreUsuario = authentication.getName();
		// Usuarios usuario = usuariosService.findByCorreo(nombreUsuario);
		// model.addAttribute("activoEmpresa", "active");
		// model.addAttribute("nombreRol", usuario.getRoles().getNombre());
		// model.addAttribute("nombreUsuario", usuario.getNombre() + " " +
		// usuario.getApellido());
		model.addAttribute("listarMateriales", materiales);
		model.addAttribute("material", new Material());
		return "materiales";
	}

	@PostMapping("/cargar")
	public String cargarMateriales(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			List<Material> materiales = service.cargarMaterialesDesdeExcel(file);
			service.guardarMateriales(materiales);
			redirectAttributes.addFlashAttribute("message", "Archivo cargado y materiales guardados con éxito.");
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Hubo un problema al cargar el archivo: " + e.getMessage());
		}
		return "redirect:/materiales";
	}

	@PostMapping("/agregar")
	public String guardarMaterial(@Valid @ModelAttribute Material material, BindingResult bindingResult,
			RedirectAttributes redirectAttributes,Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				Material codigoValido = service.buscarPorCodigo(material.getCodigo());
				if (codigoValido == null) {
					Material materialGuardar = service.crearMaterial(material);
					List<Inventario> inventarios = inventarioService.buscarPorMaterial(materialGuardar);
					materialGuardar.setInventarios(inventarios);
					service.crearMaterial(material);
					redirectAttributes.addFlashAttribute("message", "El material ha sido guardada con éxito");
					/*
					 * if (materialGuardar.getTipo().equalsIgnoreCase("activo")) { MaterialActivo
					 * materialActivo = new MaterialActivo(); materialActivo.setSerie1(serie1);
					 * materialActivo.setSerie2(serie2); materialActivo.setEstado(1);
					 * materialActivo.setMaterial(materialGuardar);
					 * materialActivoService.crearMaterialActivo(materialActivo);
					 */
				}
				else if (codigoValido != null && codigoValido.getEstado() == 0) {
					material.setEstado(1);
					service.actualizarMaterial(codigoValido.getId(), material);
					redirectAttributes.addFlashAttribute("message", "El material ha sido guardado con éxito.");
				} else {
					redirectAttributes.addFlashAttribute("message", "El material ya existe.");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/materiales";

		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/materiales";
		}
	}

	@GetMapping("/editar/{id}")
	@ResponseBody
	public Material formularioEditarMaterial(@PathVariable Long id) {
		Material material = service.obtenerPorId(id);
		return material;
	}

	@PutMapping("editar/{id}")
	public String editarMaterial(@PathVariable Long id, @Valid @ModelAttribute Material material,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (!bindingResult.hasErrors()) {
			try {
				Material materialActual = service.obtenerPorId(id);
				if (materialActual != null) {
					materialActual.setCodigo(material.getCodigo());
					materialActual.setNombre(material.getNombre());
					materialActual.setTipo(material.getTipo());
					materialActual.setEstado(material.getEstado());

					//if ("activo".equalsIgnoreCase(materialActual.getTipo())) {
						// List<MaterialActivo> materialActivos =
						// materialActivoService.buscarPorMaterial(materialActual);
						// MaterialActivo materialActivo;
						// if (materialActivos.isEmpty()) {
						// materialActivo = new MaterialActivo();
						// materialActivo.setMaterial(materialActual);
						// } else {
						// materialActivo = materialActivos.get(0);
						// }
						// materialActivo.setSerie1(serie1);
						// materialActivo.setSerie2(serie2);
						// materialActivo.setEstado(material.getEstado());
						// materialActivoService.guardarMaterialActivo(materialActivo);
					//}

					service.actualizarMaterial(id, materialActual);
					redirectAttributes.addFlashAttribute("message", "El material ha sido modificado con éxito");
				} else {
					redirectAttributes.addFlashAttribute("errorMessage", "El material no existe.");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage",
					"Error: " + bindingResult.getFieldError().getDefaultMessage());
		}
		return "redirect:/materiales";
	}

	@DeleteMapping("/eliminar/{id}")
	public String eliminarMaterial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		boolean result = service.eliminarMaterial(id);
		if (result) {
			redirectAttributes.addFlashAttribute("message", "El material ha sido eliminado con éxito.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Hubo un problema al eliminar el material.");
		}
		return "redirect:/materiales";
	}
}
