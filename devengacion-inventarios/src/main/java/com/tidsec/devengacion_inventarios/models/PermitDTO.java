package com.tidsec.devengacion_inventarios.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PermitDTO {
    private Integer id;
	
	private Integer modules_id;
	
	private Integer roles_id;

	private boolean read;

	private boolean write;

	private boolean update;

	private boolean delete;
}
