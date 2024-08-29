package com.tidsec.devengacion_inventarios.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name= "users")
public class Users {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotBlank
	private String name;
	@NotBlank
	private String lastName;
	@NotBlank
	private String email;
	private int phone;
	@NotBlank
	private String password;
    @ManyToOne
    @JoinColumn(name = "rolId", referencedColumnName = "id")
    private Role role;
    
    @OneToMany(mappedBy = "users")
    private List<Operations> operations;

    @OneToMany(mappedBy = "users")
    private List<Accrual> accruals;
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int state;
	

}
