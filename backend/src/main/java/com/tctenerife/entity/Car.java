package com.tctenerife.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que representa un coche registrado en el sistema
 */
@Entity
@Table(name = "cars", indexes = {
    @Index(name = "idx_vin", columnList = "vin", unique = true),
    @Index(name = "idx_company", columnList = "company"),
    @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // VIN = Número de chasis 
    @Column(nullable = false, unique = true, length = 17)
    private String vin;
    
    @Column(nullable = false)
    private String make; // Marca (ej: Toyota)
    
    @Column(nullable = false)
    private String model; // Modelo (ej: Corolla)
    
    @Column(nullable = false)
    private String company; // Empresa que recoge el coche
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = true)
    private LocalDateTime updatedAt;
    
    @Column(nullable = false)
    private Long createdByUserId; // ID del usuario que registró
    
    @Column(nullable = true)
    private Long updatedByUserId; // ID del usuario que actualizó
    
    // Campo para controlar sincronización offline
    @Column(nullable = false)
    private Boolean isSynced = false;
    
    // Timestamp para sincronización
    @Column(nullable = false)
    private LocalDateTime lastSyncAttempt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastSyncAttempt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}