package com.tctenerife.repository;


import com.tctenerife.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad User
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Buscar por email
    Optional<User> findByEmail(String email);
    
    // Buscar por email y activo
    Optional<User> findByEmailAndActiveTrue(String email);
    
    // Listar todos los usuarios activos
    List<User> findByActiveTrue();
    
    // Contar usuarios activos
    @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
    Long countActiveUsers();
    
    // Buscar por rol
    List<User> findByRole(User.UserRole role);
    
    // Buscar admins activos
    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN' AND u.active = true")
    List<User> findActiveAdmins();
}