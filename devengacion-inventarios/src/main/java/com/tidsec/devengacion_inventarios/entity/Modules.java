package com.tidsec.devengacion_inventarios.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "modules")
public class Modules {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @NotBlank
	@NotNull
	@Size(min = 3, max = 50)
	@Column(unique= true)
	private String name;

    @NotBlank
	@NotNull
	@Size(min = 3, max = 100)
	private String description;

	@NotBlank
	@NotNull
	@Size(min = 3, max = 100)
	private String endpoint;
    
	@Column(columnDefinition = "Integer default 1")
	private Integer status;

	@OneToMany(mappedBy="modules",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Permits> permits = new ArrayList<Permits>();

}
