package com.tidsec.devengacion_inventarios.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "serial")
public class Serial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String serialNumber1;
    @Column(nullable = true)
    private String serialNumber2;


    @ManyToOne
    @JoinColumn(name = "inventory_id", referencedColumnName = "id", nullable = false)
    private Inventory inventory;

    @Column(nullable = false)
    private String status;
    
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int state;
}