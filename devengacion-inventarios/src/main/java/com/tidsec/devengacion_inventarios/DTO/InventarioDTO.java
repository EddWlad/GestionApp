package com.tidsec.devengacion_inventarios.DTO;

import java.util.List;

import lombok.Data;

@Data
public class InventarioDTO {
    private String fechaCarga;
    private Long grupoTecnico;
    private Integer estado;
    private List<InventarioItem> materiales;

    @Data
    public static class InventarioItem {
        private Long id;  // Cambia de String codigo a Long id
        private String nombre;
        private String tipo;
        private Integer cantidad;
        private String serie1;
        private String serie2;
    }
}
