package com.tctenerife.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Entidad que registra las sincronizaciones offline
 */
@Entity
@Table(name = "sync_logs", indexes = {
    @Index(name = "idx_user", columnList = "user_id"),
    @Index(name = "idx_car", columnList = "car_id"),
    @Index(name = "idx_timestamp", columnList = "sync_timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SyncLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "car_id", nullable = false)
    private Long carId;
    
    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SyncAction action; // CREATE, UPDATE, DELETE
    
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String carData; // JSON con datos del coche
    
    @Column(nullable = false)
    private LocalDateTime syncTimestamp;
    
    @Column(nullable = true)
    private LocalDateTime deviceTimestamp; // Timestamp del dispositivo cuando se registró offline.
    
    @Column(nullable = false)
    private Boolean synced = false;
    
    @Column(nullable = true)
    private String syncResult; // OK, ERROR
    
    @Column(nullable = true, columnDefinition = "LONGTEXT")
    private String errorMessage;
    
    @PrePersist
    protected void onCreate() {
        this.syncTimestamp = LocalDateTime.now();
    }
    
    /**
     * Te deja Crear, actualizar, eliminar y si  hay algún problema te tira un mensaje de error.
     */
    public enum SyncAction {
        CREATE("Crear"),
        UPDATE("Actualizar"),
        DELETE("Eliminar"),
        CONFLICT("Conflicto");
        
        private final String descripcion;
        
        SyncAction(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}