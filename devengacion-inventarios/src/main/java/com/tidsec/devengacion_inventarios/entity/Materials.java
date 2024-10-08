package com.tidsec.devengacion_inventarios.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@Table(name= "materials")
public class Materials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer code;
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int state;
    
    @OneToMany(mappedBy = "materials")
    private List<Serial> serial;

    @ManyToMany(mappedBy = "materials")
    private List<Inventory> inventory;
}
