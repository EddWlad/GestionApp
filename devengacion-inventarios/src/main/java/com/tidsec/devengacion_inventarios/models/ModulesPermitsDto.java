package com.tidsec.devengacion_inventarios.models;

import com.tidsec.devengacion_inventarios.entity.Permits;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ModulesPermitsDto {
    private Integer moduleId;
    private String moduleName;
    private Permits permits;
}
