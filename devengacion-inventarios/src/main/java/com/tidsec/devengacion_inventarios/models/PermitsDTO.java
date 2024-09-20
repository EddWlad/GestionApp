package com.tidsec.devengacion_inventarios.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class PermitsDTO {
    private List<PermitDTO> permits;
}
