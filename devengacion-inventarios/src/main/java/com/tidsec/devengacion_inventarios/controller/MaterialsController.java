package com.tidsec.devengacion_inventarios.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tidsec.devengacion_inventarios.entity.Materials;
import com.tidsec.devengacion_inventarios.service.IMaterialsService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequestMapping("/materials")
public class MaterialsController {
	@Autowired
	private IMaterialsService materialsService;
	
	@GetMapping
	public String listMaterials(Model model) {
		List<Materials> materials = materialsService.getAll();
		model.addAttribute("activeMaterials", "active");
		model.addAttribute("listMaterials",materials);
		model.addAttribute("materials", new Materials());
		return "materials";
	}
	
	@PostMapping("/save")
	public String saveMaterials(@Valid @ModelAttribute Materials materials, BindingResult bindingResult,
		   RedirectAttributes redirectAttributes, Model model) {
		if (!bindingResult.hasErrors()) {
			try {
				Materials nameValid = materialsService.findByName(materials.getName());
				Materials codeValid = materialsService.findByCode(materials.getCode());
				if (nameValid == null || codeValid == null) {
					Materials materialsSave = materialsService.saveMaterials(materials);
					materialsService.saveMaterials(materialsSave);
					redirectAttributes.addFlashAttribute("message", "MATERIAL INSERTADO CORRECTAMENTE");
				} else if (nameValid != null && nameValid.getState() == 0 || codeValid != null && codeValid.getState() == 0) {
					codeValid.setCode(materials.getCode());
					codeValid.setName(materials.getName());
					codeValid.setType(materials.getType());
					codeValid.setState(1);
					materialsService.updateMaterials(codeValid.getId(), materials);
					redirectAttributes.addFlashAttribute("message", "MATERIAL INSERTADO CORRECTAMENTE");
				} else {
					redirectAttributes.addFlashAttribute("message", "EL MATERIAL YA EXISTE");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
			return "redirect:/materials";
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
			return "redirect:/materials";
		}
	}
	
	@PostMapping("/upload")
	public ResponseEntity<Map<String, String>> uploadExcel(@RequestParam("file") MultipartFile file) {
	    Map<String, String> response = new HashMap<>();
	    
	    if (file.isEmpty()) {
	        response.put("success", "false");
	        response.put("message", "NO SE HA CARGADO NINGUN ARCHIVO");
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    
	    try {
	        String contentType = file.getContentType();
	        if (!"application/vnd.ms-excel".equals(contentType) && 
	            !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
	            response.put("success", "false");
	            response.put("message", "TIPO DE ARCHIVO NO SOPORTADO");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	        }
	        
	        materialsService.uploadMaterialsFromExcel(file);
	        
	        response.put("success", "true");
	        response.put("message", "DATOS CARGADOS EXITOSAMENTE");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("success", "false");
	        response.put("message", "ERROR AL CARGAR LOS DATOS: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
	@GetMapping("/edit/{id}")
	@ResponseBody
	public Materials editFormMaterials(@PathVariable Integer id) {
		Materials materials = materialsService.getById(id);
		return materials;
	}
	
	@PutMapping("edit/{id}")
	public String editMaterials(@PathVariable Integer id, @Valid @ModelAttribute Materials materials, 
		   BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (!bindingResult.hasErrors()) {
			try {
				Materials materialsUpdate = materialsService.getById(id);
				if (materialsUpdate != null) {
					materials.setInventory(materialsUpdate.getInventory());
					materialsService.updateMaterials(id, materials);
					redirectAttributes.addFlashAttribute("message", "MATERIAL MODIFICADO EXITOSAMENTE");
				} else {
					redirectAttributes.addFlashAttribute("message", "EL MATERIAL NO EXISTE");
				}
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message", bindingResult.getFieldError().getDefaultMessage());
		}
		return "redirect:/materials";
	}
	
	@DeleteMapping("/delete/{id}")
	@ResponseBody
	public boolean deleteMaterials(@PathVariable Integer id) {
		boolean result = materialsService.deleteMaterials(id);
		if (result) {
			return true;
		} else {
			return false;
		}
	}
}
