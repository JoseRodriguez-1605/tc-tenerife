package com.tctenerife.repository;
 
import com.tctenerife.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 
import java.util.List;
import java.util.Optional;
 
/**
 * Repositorio para la entidad Usuario
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Buscar por email
    Optional<Usuario> findByEmail(String email);
    
    // Buscar por email y activo
    Optional<Usuario> findByEmailAndActivoTrue(String email);
    
    // Listar todos los usuarios activos
    List<Usuario> findByActivoTrue();
    
    // Contar usuarios activos
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    Long countActivosTrue();
    
    // Buscar por rol
    List<Usuario> findByRol(Usuario.Rol rol);
}