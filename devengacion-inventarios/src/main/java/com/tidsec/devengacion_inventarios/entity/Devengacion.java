package com.tidsec.devengacion_inventarios.entity;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.micrometer.common.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name= "devengacion")
public class Devengacion 
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_inventario", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Inventario inventario;

    @NotBlank
    private String proyectoId;

    @NotBlank
    private String cluster;
    
    @NotBlank
    private String tipoInstalacion;
    
    @NotNull
    private Integer cantidad;
    
    @NotNull
	@Column(columnDefinition = "Integer default 1")
    private Integer estado;

    @Nullable
    @Temporal(TemporalType.DATE)
    private Date fechaDevengacion = new Date();
    
    @Nullable
    @Temporal(TemporalType.DATE)
    private Date fechaInstalacion = new Date();
    
    
}
