package com.tidsec.devengacion_inventarios.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name= "inventario")
public class Inventario 
{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_grupoTecnico", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private GrupoTecnico grupoTecnico;

    @ManyToOne
    @JoinColumn(name = "id_material", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Material material;

    @NotNull
    private Integer cantidad;

    @Nullable
    @Temporal(TemporalType.DATE)
    private Date fechaCarga = new Date();

    @NotNull
	@Column(columnDefinition = "Integer default 1")
    private Integer estado;
    

    @OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<MaterialActivo> materialesActivos = new ArrayList<>();
    
	@OneToMany(mappedBy = "inventario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<Devengacion> devengaciones = new ArrayList<Devengacion>();
	
    public void addMaterialActivo(MaterialActivo materialActivo) {
        this.materialesActivos.add(materialActivo);
        materialActivo.setInventario(this);
    }
    
    public List<MaterialActivo> getMaterialesActivos() {
        return materialesActivos;
    }
}
