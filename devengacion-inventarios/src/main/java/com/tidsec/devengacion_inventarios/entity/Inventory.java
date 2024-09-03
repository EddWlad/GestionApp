package com.tidsec.devengacion_inventarios.entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name= "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateInstalation;
    @NotBlank
    private String cluster;
    @NotBlank
    private String proyectId;
    @NotBlank
    private String typeInstalation;
    @NotNull
    private Integer amount;
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int state;

    @ManyToOne
    @JoinColumn(name = "usersId", referencedColumnName = "id")
    private Users users;
    
    @ManyToMany
    private List<Materials> materials;

    @ManyToMany
    @JoinTable(
      name = "accrualInventory",
      joinColumns = @JoinColumn(name = "accrualId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "inventoryId", referencedColumnName = "id"))
    private List<Inventory> inventories;
}