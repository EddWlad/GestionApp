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

import com.tidsec.devengacion_inventarios.entity.TechnicalGroup;
import com.tidsec.devengacion_inventarios.service.ITechnicalGroupService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/technicalGroups")
public class TechnicalGroupController {
	@Autowired
	private ITechnicalGroupService service;
	
	//@Autowired
	//private IInventoryService inventoryService;
	
	@GetMapping
	public String listTechnicalGroup(Model model) {
		List<TechnicalGroup> technicalGroup = service.getAll();
		model.addAttribute("activeTechnicalGroups", "active");
		// Authentication authentication =
		// SecurityContextHolder.getContext().getAuthentication();
		// String nombreUsuario = authentication.getName();
		// Usuarios usuario = usuariosService.findByCorreo(nombreUsuario);
		// model.addAttribute("activoEmpresa", "active");
		// model.addAttribute("nombreRol", usuario.getRoles().getNombre());
		// model.addAttribute("nombreUsuario", usuario.getNombre() + " " +
		// usuario.getApellido());
		model.addAttribute("listTechnicalGroup", technicalGroup);
		model.addAttribute("technicalGroup", new TechnicalGroup());
		return "technicalGroups";
	}
	
	@PostMapping("/save")
	public String saveTechnicalGroup(@Valid @ModelAttribute TechnicalGroup technicalGroup, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				TechnicalGroup codeValid = service.findByName(technicalGroup.getName());
				if (codeValid  == null) {
					TechnicalGroup technicalGroupSave = service.saveTechnicalGroup(technicalGroup);
					//List<Inventory> inventory = inventoryService.findByTechnicalGroup(technicalGroupSave);
					//technicalGroupSave.setInventory(inventory);
					service.saveTechnicalGroup(technicalGroup);
					redirectAttributes.addFlashAttribute("message", "GRUPO CREADO CON EXITO");
				} else if (codeValid != null && codeValid.getState() == 0) {
					technicalGroup.setState(1);
					service.updateTechnicalGroup(codeValid.getId(), technicalGroup);
					redirectAttributes.addFlashAttribute("message", "GRUPO CREADO CON EXITO");
				} else {
					redirectAttributes.addFlashAttribute("message", "EL GRUPO EXISTE");
				}

			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/technicalGroups";
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/technicalGroups";
		}
	}
	
	@GetMapping("/edit/{id}")
	@ResponseBody
	public TechnicalGroup editFormTechnicalGroup(@PathVariable Integer id) {
		TechnicalGroup technicalGroup = service.getById(id);
		return technicalGroup;
	}
	
	@PutMapping("edit/{id}")
	public String editTechnicalGroup(@PathVariable Integer id, @Valid @ModelAttribute TechnicalGroup technicalGroup,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (!bindingResult.hasErrors()) {
			try {
				TechnicalGroup technicalGroupUpdate = service.getById(id);
				if (technicalGroupUpdate != null) {
					technicalGroup.setInventory(technicalGroupUpdate.getInventory());
					service.updateTechnicalGroup(id, technicalGroup);
					redirectAttributes.addFlashAttribute("message", "GRUPO MODIFICADO CON EXITO");
				} else {
					redirectAttributes.addFlashAttribute("message", "EL GRUPO NO EXISTE");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
		}
		return "redirect:/technicalGroups";
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseBody
		public boolean deleteTechnicalGroup(@PathVariable Integer id) {
			boolean result = service.deleteTechnicalGroup(id);
			if (result) {
				return true;
			} else {
				return false;
			}
		}
	
}
