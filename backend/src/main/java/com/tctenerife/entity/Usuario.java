package com.tctenerife.entity;
 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
 
/**
 * Entidad que representa un usuario del sistema
 * Roles: ADMIN o USUARIO
 */
@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "idx_email", columnList = "email", unique = true),
    @Index(name = "idx_rol", columnList = "rol")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false)
    private String password; // Hash BCrypt
    
    @Column(nullable = false, length = 100, name = "nombre_completo")
    private String nombreCompleto;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rol rol; // ADMIN o USUARIO
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(nullable = false, name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(nullable = true, name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @Column(nullable = true, name = "ultimo_login")
    private LocalDateTime ultimoLogin;
    
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    /**
     * Enum para roles de usuario
     */
    public enum Rol {
        ADMIN("Administrador"),
        USUARIO("Usuario");
        
        private final String descripcion;
        
        Rol(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
}