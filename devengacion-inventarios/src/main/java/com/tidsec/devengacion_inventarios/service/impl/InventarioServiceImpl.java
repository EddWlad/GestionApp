package com.tidsec.devengacion_inventarios.service.impl;

import java.io.IOException;
import java.io.InputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.text.ParseException;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tidsec.devengacion_inventarios.entity.Inventario;
import com.tidsec.devengacion_inventarios.entity.Material;
import com.tidsec.devengacion_inventarios.entity.MaterialActivo;
import com.tidsec.devengacion_inventarios.entity.GrupoTecnico;
import com.tidsec.devengacion_inventarios.repository.IInventarioRepository;
import com.tidsec.devengacion_inventarios.service.IInventarioService;
import com.tidsec.devengacion_inventarios.service.IMaterialService;
import com.tidsec.devengacion_inventarios.service.IGrupoTecnicoService;

@Service
public class InventarioServiceImpl implements IInventarioService {

    @Autowired
    private IInventarioRepository inventarioRepository;

    @Autowired
    private IMaterialService materialService;

    @Autowired
    private IGrupoTecnicoService grupoTecnicoService;

    @Override
    public List<Inventario> cargarInventariosDesdeExcel(MultipartFile file) throws IOException {
        List<Inventario> inventarios = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip the header row
            }
            Inventario inventario = new Inventario();
            
            // Buscar Grupo Técnico por Nombre
            String grupoCodigo = getCellValue(row.getCell(0));
            Optional<GrupoTecnico> grupoTecnicoList = grupoTecnicoService.buscarOptionalPorCodigo(grupoCodigo);
            if (grupoTecnicoList.isEmpty()) {
                // handle error: group not found
                continue;
            }
            GrupoTecnico grupoTecnico = grupoTecnicoList.get(); // Usar el primer resultado
            inventario.setGrupoTecnico(grupoTecnico);

            // Buscar Material por Nombre
            String materialNombre = getCellValue(row.getCell(1));
            Optional<Material> materialList = materialService.buscarPorNombre(materialNombre);
            if (materialList.isEmpty()) {
                // handle error: material not found
                continue;
            }
            Material material = materialList.get(); // Usar el primer resultado
            inventario.setMaterial(material);

            inventario.setCantidad(Integer.parseInt(getCellValue(row.getCell(2))));
            
            // Manejar la conversión de la fecha
            String fechaCargaStr = getCellValue(row.getCell(3));
            try {
                java.util.Date parsedDate = dateFormat.parse(fechaCargaStr);
                inventario.setFechaCarga(new java.sql.Date(parsedDate.getTime()));
            } catch (ParseException e) {
                // handle error: invalid date format
                continue;
            }

            inventario.setEstado(1); // Set the default state to active

            // Handle active materials with series
            if (material.getTipo().equalsIgnoreCase("activo")) {
                MaterialActivo materialActivo = new MaterialActivo();
                materialActivo.setSerie1(getCellValue(row.getCell(4)));
                materialActivo.setSerie2(getCellValue(row.getCell(5)));
                materialActivo.setEstado(1);
                inventario.addMaterialActivo(materialActivo);
            }

            inventarios.add(inventario);
        }
        workbook.close();
        return inventarios;
    }

    @Override
    public String getCellValue(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                return dateFormat.format(cell.getDateCellValue());
            } else {
                return String.valueOf((int) cell.getNumericCellValue());
            }
        } else {
            return "";
        }
    }

    @Override
    public void guardarInventarios(List<Inventario> inventarios) {
        for (Inventario inventario : inventarios) {
            inventarioRepository.save(inventario);
        }
    }

    @Override
    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findByEstadoNot(0);
    }

    @Override
    public Inventario obtenerPorId(Long id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Inventario> buscarPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    @Override
    public Inventario crearInventario(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    @Override
    public Inventario actualizarInventario(Long id, Inventario inventario) {
        Inventario inventarioDb = inventarioRepository.findById(id).orElse(null);
        if (inventarioDb != null) {
            inventarioDb.setGrupoTecnico(inventario.getGrupoTecnico());
            inventarioDb.setMaterial(inventario.getMaterial());
            inventarioDb.setCantidad(inventario.getCantidad());
            inventarioDb.setFechaCarga(inventario.getFechaCarga());
            inventarioDb.setEstado(inventario.getEstado());
            return inventarioRepository.save(inventarioDb);
        } else {
            return null;
        }
    }

    @Override
    public boolean eliminarInventario(Long id) {
        Inventario inventarioDb = inventarioRepository.findById(id).orElse(null);
        if (inventarioDb != null) {
            inventarioDb.setEstado(0);
            inventarioRepository.save(inventarioDb);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Long contarInventario() {
        return inventarioRepository.count();
    }

	@Override
	public List<Inventario> buscarPorGrupoTecnico(GrupoTecnico grupoTecnico) {
		return inventarioRepository.findByGrupoTecnico(grupoTecnico) ;
	}

	@Override
	public List<Inventario> buscarPorMaterial(Material material) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void guardarMaterialActivo(MaterialActivo materialActivo) {
		// TODO Auto-generated method stub
		
	}
	

    @Override
    public List<Inventario> obtenerMaterialesPorGrupo(Long grupoTecnicoId) {
        return inventarioRepository.findByGrupoTecnicoId(grupoTecnicoId);
    }

    @Override
    public void eliminarPorSerie(String codigoMaterial) {
        List<Inventario> inventarios = inventarioRepository.findByMaterialCodigo(codigoMaterial);
        for (Inventario inventario : inventarios) {
            for (MaterialActivo materialActivo : inventario.getMaterialesActivos()) {
                materialActivo.setEstado(0); // Estado 0 para borrado lógico
            }
            inventario.setEstado(0); // Estado 0 para borrado lógico
            inventarioRepository.save(inventario);
        }
    }

    @Override
    public void decrementarCantidad(Long grupoTecnicoId, Long materialId, Integer cantidad) {
        Inventario inventario = inventarioRepository.findByGrupoTecnicoIdAndMaterialId(grupoTecnicoId, materialId);
        if (inventario != null) {
            inventario.setCantidad(inventario.getCantidad() - cantidad);
            inventarioRepository.save(inventario);
        }
    }
}