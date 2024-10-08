package com.tidsec.devengacion_inventarios.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name= "role")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	private String name;
	@NotBlank
	private String description;
    
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int status;

	@OneToMany(mappedBy="role",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
 @JsonBackReference
	private List<Users> usuarios = new ArrayList<Users>();
	
	@OneToMany(mappedBy="role",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Permits> permits = new ArrayList<Permits>();
}
