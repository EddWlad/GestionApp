package com.tidsec.devengacion_inventarios.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name= "operations")
public class Operations {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank
	private String typeOperation;
	@NotBlank
	private String codeProyect;
	@NotBlank
	private long proyectoId;
	
    @ManyToOne
    @JoinColumn(name = "inventoryId", referencedColumnName = "id")
    private Inventory inventory;
	
	@NotBlank
	private boolean optimalPower;
	@NotBlank
	private String cluster;
	@NotBlank
	private String napNumber;
	@NotBlank
	private String napPort;
	@NotBlank
	private String sealSerial;
	@NotBlank
	private String latitude;
	@NotBlank
	private String length;
	@NotBlank
	private double mettersUsed;
	@NotBlank
	private String instalationAttends;

    @ManyToOne
    @JoinColumn(name = "usersId", referencedColumnName = "id")
    private Users users;
	
	@NotBlank
	private String observations;
    
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int state;
}
