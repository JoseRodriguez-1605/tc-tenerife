package com.tctenerife.entity;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
 
/**
 * Entidad que representa un registro de recogida de vehículo
 */
@Entity
@Table(name = "control_recogida", indexes = {
    @Index(name = "idx_registro", columnList = "registro"),
    @Index(name = "idx_empresa", columnList = "empresa"),
    @Index(name = "idx_vin", columnList = "vin", unique = true),
    @Index(name = "idx_estado", columnList = "estado"),
    @Index(name = "idx_fecha_hora", columnList = "fecha_hora"),
    @Index(name = "idx_usuario", columnList = "usuario_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ControlRecogida {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, name = "registro")
    private Integer registro;
    
    @Column(nullable = false, length = 100)
    private String empresa;
    
    @Column(nullable = false, length = 50, unique = true)
    private String vin; // Número de chasis
    
    @Column(nullable = false, length = 50)
    private String marca;
    
    @Column(nullable = false, length = 50)
    private String modelo;
    
    @Column(nullable = false, length = 100)
    private String buque;
    
    @Column(nullable = true, length = 20)
    private String matricula;
    
    @Column(nullable = false, name = "fecha_hora")
    private LocalDateTime fechaHora;
    
    @Column(nullable = true, name = "id_barco")
    private Integer idBarco;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.ENTREGADO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;
    
    @PrePersist
    protected void onCreate() {
        if (this.fechaHora == null) {
            this.fechaHora = LocalDateTime.now();
        }
    }
    
    /**
     * Enum para estados de recogida
     */
    public enum Estado {
        ENTREGADO("Entregado"),
        DEVUELTO("Devuelto");
        
        private final String descripcion;
        
        Estado(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}