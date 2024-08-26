package com.tidsec.devengacion_inventarios.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tidsec.devengacion_inventarios.entity.Devengacion;
import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;
import com.tidsec.devengacion_inventarios.service.IDevengacionService;
import com.tidsec.devengacion_inventarios.service.IGrupoTecnicoService;

@Controller
@RequestMapping("/devengaciones")
public class DevengacionController {
	@Autowired
	private IDevengacionService service;
	@Autowired
	private IGrupoTecnicoService grupoTecnicoService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
	
	@GetMapping("/grupos")
	@ResponseBody
	public List<GrupoTecnico> obtenerGrupoTecnico(Model model) {
		try{
			return grupoTecnicoService.obtenerTodos();
		}catch(Exception e){
			return null;
		}
	}

	@GetMapping("/grupos/{id}")
	@ResponseBody
	public GrupoTecnico obtenerDetallesGrupoTecnicoMaterial(@PathVariable Long id) {
		return grupoTecnicoService.obtenerPorId(id);
	}
	
	@GetMapping
	public String listarEtiqueta(Model model) {
		List<Devengacion> devengacion = service.obtenerTodas();
		model.addAttribute("activoDevengaciones", "active");
		model.addAttribute("listarDevengaciones", devengacion);
		return "devengaciones";
	}
}
