package com.tctenerife.entity;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
 
/**
 * Entidad que registra las sincronizaciones y cambios
 */
@Entity
@Table(name = "sincronizacion_logs", indexes = {
    @Index(name = "idx_usuario", columnList = "usuario_id"),
    @Index(name = "idx_recogida", columnList = "recogida_id"),
    @Index(name = "idx_fecha", columnList = "fecha_sincronizacion")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SincronizacionLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recogida_id", nullable = true)
    private ControlRecogida recogida;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Accion accion;
    
    @Column(nullable = false, columnDefinition = "LONGTEXT", name = "datos_json")
    private String datosJson;
    
    @Column(nullable = false, name = "fecha_sincronizacion")
    private LocalDateTime fechaSincronizacion;
    
    @Column(nullable = true, name = "fecha_dispositivo")
    private LocalDateTime fechaDispositivo;
    
    @Column(nullable = false, name = "sincronizado")
    private Boolean sincronizado = false;
    
    @Column(nullable = true, name = "resultado")
    private String resultado;
    
    @Column(nullable = true, columnDefinition = "LONGTEXT", name = "mensaje_error")
    private String mensajeError;
    
    @PrePersist
    protected void onCreate() {
        this.fechaSincronizacion = LocalDateTime.now();
    }
    
    /**
     * Enum para tipos de acción
     */
    public enum Accion {
        CREAR("Crear"),
        ACTUALIZAR("Actualizar"),
        ELIMINAR("Eliminar");
        
        private final String descripcion;
        
        Accion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}