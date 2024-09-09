package com.tidsec.devengacion_inventarios.entity;

import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name= "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @NotBlank
    private String observation;
    @ManyToOne
    @JoinColumn(name = "group_origin_id", referencedColumnName = "id")
    private TechnicalGroup groupOrigin;

    @ManyToOne
    @JoinColumn(name = "group_destinity_id", referencedColumnName = "id")
    private TechnicalGroup groupDestinity;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;
    @NotNull
    @Column(columnDefinition = "Integer default 1")
	private int state;
}
