package com.tctenerife.entity;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * Entidad que representa un vehículo del catálogo
 */
@Entity
@Table(name = "vehiculos", indexes = {
    @Index(name = "idx_marca", columnList = "marca"),
    @Index(name = "idx_modelo", columnList = "modelo"),
    @Index(name = "idx_buque", columnList = "buque")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {
    
    @Id
    @Column(nullable = false, length = 50)
    private String chassis; // Número de chasis (VIN)
    
    @Column(nullable = false, length = 50)
    private String marca;
    
    @Column(nullable = false, length = 50)
    private String modelo;
    
    @Column(nullable = false, length = 100)
    private String buque;
}