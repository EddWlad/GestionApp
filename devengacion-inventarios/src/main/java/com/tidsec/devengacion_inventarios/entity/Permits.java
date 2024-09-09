package com.tidsec.devengacion_inventarios.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "permits")
public class Permits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	private Role roles;
	
	@ManyToOne
	@JoinColumn(name = "modules_id")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	private Modules modules;
	
	@Column(columnDefinition = "boolean default false")
	private boolean read;
	
	@Column(columnDefinition = "boolean default false")
	private boolean write;
	
	@Column(columnDefinition = "boolean default false")
	private boolean update;
	
	@Column(columnDefinition = "boolean default false")
	private boolean delete;
}
