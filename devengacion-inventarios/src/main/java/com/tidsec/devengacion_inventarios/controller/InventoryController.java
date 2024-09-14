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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tidsec.devengacion_inventarios.entity.Inventory;
import com.tidsec.devengacion_inventarios.service.IInventoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/inventories")
public class InventoryController {
	@Autowired
	private IInventoryService service;
	
	@GetMapping
	public String listInventory(Model model) {
		List<Inventory> inventories = service.getAllActiveInventories();
		model.addAttribute("activeInventories", "active");
		model.addAttribute("listInventories",inventories);
		model.addAttribute("inventories", new Inventory());
		return "inventories";
	}
	
    @PostMapping("/save")
    public String saveInventory(@Valid @ModelAttribute Inventory inventory, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                service.saveInventory(inventory);
                redirectAttributes.addFlashAttribute("SUCCESS", "Inventario guardado correctamente.");
                return "redirect:/inventory";
            } catch (Exception e) {
                model.addAttribute("ERROR", "Error al guardar el inventario: " + e.getMessage());
            }
        } else {
            model.addAttribute("error", "Error en la validación del inventario.");
        }
        return "redirect:/inventories"; 
    }
    
	@GetMapping("/edit/{id}")
	@ResponseBody
	public Inventory editFormInventories(@PathVariable Integer id) {
		Inventory inventories = service.getById(id);
		return inventories;
	}
    
	@PutMapping("edit/{id}")
    public String updateInventory(@Valid @ModelAttribute Inventory inventory, BindingResult bindingResult,
                                  RedirectAttributes redirectAttributes, Model model) {
        if (!bindingResult.hasErrors()) {
            try {
                service.updateInventory(inventory.getId(), inventory);
                redirectAttributes.addFlashAttribute("success", "Inventario actualizado correctamente.");
                return "redirect:/inventory";
            } catch (Exception e) {
                model.addAttribute("ERROR", "Error al actualizar el inventario: " + e.getMessage());
            }
        } else {
            model.addAttribute("ERROR", "Error en la validación del inventario.");
        }
        return "redirect:/inventories";
    }
    
	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public boolean deleteInventories(@PathVariable Integer id) {
		boolean result = service.deleteInventory(id);
		if (result) {
			return true;
		} else {
			return false;
		}
	}
	
    @PostMapping("/upload")
    public String uploadMaterialsFromExcel(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            service.loadMaterialsFromExcel(file);
            redirectAttributes.addFlashAttribute("SUCCESS", "Materiales cargados correctamente desde el archivo Excel.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("ERROR", "Error al cargar el archivo Excel: " + e.getMessage());
        }
        return "redirect:/inventories";
    }
}
