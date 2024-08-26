package com.tidsec.devengacion_inventarios.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tidsec.devengacion_inventarios.DTO.InventarioDTO;
import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;
import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.entity.Material;
import com.tidsec.devengacion_inventarios.entity.MaterialActivo;
import com.tidsec.devengacion_inventarios.service.IGrupoTecnicoService;
import com.tidsec.devengacion_inventarios.service.IInventarioService;
import com.tidsec.devengacion_inventarios.service.IMaterialActivoService;
//import com.tidsec.devengacion_inventarios.service.IMaterialService;
//import com.tidsec.devengacion_inventarios.service.IGrupoTecnicoService;
import com.tidsec.devengacion_inventarios.service.IMaterialService;


import jakarta.validation.Valid;

@Controller
@RequestMapping("/inventarios")
public class InventarioController {
    
    @Autowired
    private IInventarioService inventarioService;

    @Autowired
    private IMaterialService materialService;
    
    @Autowired
    private IMaterialActivoService materialActivoService;

    @Autowired
    private IGrupoTecnicoService grupoTecnicoService;

    @GetMapping
    public String listarInventarios(Model model) {
        List<Inventario> inventarios = inventarioService.obtenerTodos();
        for (Inventario inventario : inventarios) {
            if ("activo".equalsIgnoreCase(inventario.getMaterial().getTipo())) {
                List<MaterialActivo> materialesActivos = materialActivoService.buscarPorInventario(inventario);
                if (!materialesActivos.isEmpty()) {
                    inventario.setMaterialesActivos(materialesActivos);
                }
            }
        }
        model.addAttribute("activoInventarios", "active");
        model.addAttribute("listarInventarios", inventarios);
        model.addAttribute("inventario", new Inventario());
        return "inventarios";
    }
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
    @PostMapping("/cargar")
    public String cargarInventarios(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            List<Inventario> inventarios = inventarioService.cargarInventariosDesdeExcel(file);
            inventarioService.guardarInventarios(inventarios);
            redirectAttributes.addFlashAttribute("message", "Archivo cargado y inventarios guardados con éxito.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un problema al cargar el archivo: " + e.getMessage());
        }
        return "redirect:/inventarios";
    }

    
	@GetMapping("/grupos")
	@ResponseBody
	public List<GrupoTecnico> obtenerGruposTecnicos(Model model) {
		try
		{
			return grupoTecnicoService.obtenerTodos();
		}
		catch(Exception e)
		{
			return null;
		}

	}
	
	@GetMapping("/materiales")
	@ResponseBody
	public List<Material> obtenerMateriales(Model model) {
		try
		{
			return materialService.obtenerTodos();
		}
		catch(Exception e)
		{
			return null;
		}

	}
	
	@GetMapping("/materiales/{id}")
	@ResponseBody
	public Material obtenerTipoMaterial(@PathVariable Long id) {
		return materialService.obtenerPorId(id);
	}
    
    
	@PostMapping("/agregar")
	public ResponseEntity<String> guardarInventario(@RequestBody InventarioDTO inventarioDTO) {
	    try {
	        // Validación de entrada
	        if (inventarioDTO.getFechaCarga() == null || inventarioDTO.getGrupoTecnico() == null || 
	            inventarioDTO.getEstado() == null || inventarioDTO.getMateriales() == null || 
	            inventarioDTO.getMateriales().isEmpty()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos incompletos");
	        }

	        // Obtener los datos del formulario
	        Date fechaCarga = new SimpleDateFormat("yyyy-MM-dd").parse(inventarioDTO.getFechaCarga());
	        GrupoTecnico grupoTecnico = grupoTecnicoService.buscarPorId(inventarioDTO.getGrupoTecnico()).orElse(null);
	        if (grupoTecnico == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Grupo Técnico no encontrado");
	        }
	        Integer estado = inventarioDTO.getEstado();

	        for (InventarioDTO.InventarioItem item : inventarioDTO.getMateriales()) {
	            // Validar los datos del material
	            if (item.getId() == null || item.getCantidad() == null || item.getTipo() == null) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Datos del material incompletos");
	            }

	            // Crear y guardar el inventario
	            Inventario inventario = new Inventario();
	            inventario.setFechaCarga(new java.sql.Date(fechaCarga.getTime()));
	            inventario.setGrupoTecnico(grupoTecnico);
	            inventario.setEstado(estado);
	            inventario.setCantidad(item.getCantidad());

	            // Buscar Material por ID
	            Material material = materialService.buscarPorId(item.getId()).orElse(null);
	            if (material == null) {
	                System.out.println("Material no encontrado: " + item.getId());
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Material no encontrado: " + item.getId());
	            }
	            inventario.setMaterial(material);

	            // Guardar inventario
	            inventarioService.crearInventario(inventario);

	            // Si el material es activo, guardar las series
	            if ("activo".equalsIgnoreCase(material.getTipo())) {
	                MaterialActivo materialActivo = new MaterialActivo();
	                materialActivo.setSerie1(item.getSerie1());
	                materialActivo.setSerie2(item.getSerie2());
	                materialActivo.setEstado(estado);
	                materialActivo.setInventario(inventario);
	                materialActivoService.crearMaterialActivo(materialActivo);
	            }
	        }

	        return ResponseEntity.status(HttpStatus.CREATED).body("Inventarios guardados con éxito");
	    } catch (Exception e) {
	        e.printStackTrace();  // Agregar este print para obtener más detalles del error en la consola
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar inventarios: " + e.getMessage());
	    }
	}

	
    @GetMapping("/editar/{id}")
    @ResponseBody
    public Inventario formularioEditarInventario(@PathVariable Long id) {
        Inventario inventario = inventarioService.obtenerPorId(id);
        if (inventario != null && "activo".equalsIgnoreCase(inventario.getMaterial().getTipo())) {
            List<MaterialActivo> materialesActivos = materialActivoService.buscarPorInventario(inventario);
            if (!materialesActivos.isEmpty()) {
                inventario.setMaterialesActivos(materialesActivos);
            }
        }
        return inventario;
    }

    @PutMapping("/editar/{id}")
    public String editarInventario(@PathVariable Long id, @Valid @ModelAttribute Inventario inventario,
                                   BindingResult bindingResult, @RequestParam(value = "serie1", required = false) String serie1,
                                   @RequestParam(value = "serie2", required = false) String serie2,
                                   RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()) {
            try {
                Inventario inventarioActual = inventarioService.obtenerPorId(id);
                if (inventarioActual != null) {
                    inventarioActual.setGrupoTecnico(inventario.getGrupoTecnico());
                    inventarioActual.setMaterial(inventario.getMaterial());
                    inventarioActual.setCantidad(inventario.getCantidad());
                    inventarioActual.setFechaCarga(inventario.getFechaCarga());
                    inventarioActual.setEstado(inventario.getEstado());

                    if ("activo".equalsIgnoreCase(inventarioActual.getMaterial().getTipo())) {
                        List<MaterialActivo> materialActivos = materialActivoService.buscarPorInventario(inventarioActual);
                        MaterialActivo materialActivo;
                        if (materialActivos.isEmpty()) {
                            materialActivo = new MaterialActivo();
                            materialActivo.setInventario(inventarioActual);
                        } else {
                            materialActivo = materialActivos.get(0);
                        }
                        materialActivo.setSerie1(serie1);
                        materialActivo.setSerie2(serie2);
                        materialActivo.setEstado(inventario.getEstado());
                        materialActivoService.crearMaterialActivo(materialActivo);
                    }

                    inventarioService.actualizarInventario(id, inventarioActual);
                    redirectAttributes.addFlashAttribute("message", "El inventario ha sido modificado con éxito");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "El inventario no existe.");
                }
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + bindingResult.getFieldError().getDefaultMessage());
        }
        return "redirect:/inventarios";
    }

    @DeleteMapping("/eliminar/{id}")
    public boolean eliminarInventario(@PathVariable Long id) {
        boolean result = false;
        try {
            Inventario inventario = inventarioService.obtenerPorId(id);
            if (inventario != null) {
                List<MaterialActivo> materialActivos = materialActivoService.buscarPorInventario(inventario);
                for (MaterialActivo materialActivo : materialActivos) {
                    materialActivo.setEstado(0); // Actualiza el estado a 0 (Inactivo)
                    materialActivoService.crearMaterialActivo(materialActivo); // Guarda los cambios
                }
                result = inventarioService.eliminarInventario(id);
            }
        } catch (Exception e) {
            // Manejar la excepción adecuadamente
            e.printStackTrace();
        }
        return result;
    }
}