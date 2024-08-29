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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	private int id;
	@NotNull
	private int amount;
	@NotBlank
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDownload;

    @OneToOne
    @JoinColumn(name = "technicalGroupId", referencedColumnName = "id")
    private TechnicalGroup technicalGroup;
    
    @ManyToMany
    @JoinTable(
      name = "materialInventory",
      joinColumns = @JoinColumn(name = "inventoryId", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "materialId", referencedColumnName = "id"))
    private List<Materials> materials;
    
    @ManyToMany(mappedBy = "inventories")
    private List<Accrual> accruals;

    @OneToMany(mappedBy = "inventory")
    private List<Operations> operations;
    
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int state;
}
