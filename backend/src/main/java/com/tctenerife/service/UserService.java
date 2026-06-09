package com.tctenerife.service;
import com.tctenerife.dto.*;
import com.tctenerife.entity.Car;
import com.tctenerife.entity.User;
import com.tctenerife.entity.SyncLog;
import com.tctenerife.repository.CarRepository;
import com.tctenerife.repository.UserRepository;
import com.tctenerife.repository.SyncLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;



private UserRepository userRepository;


private PasswordEncoder passwordEncoder;


private CarRepository carRepository;

// @Autowired
// private JwtTokenProvider jwtTokenProvider;

public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
  //  private JwtTokenProvider jwtTokenProvider;
    
    /**
     * Login de usuario
     */
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndActiveTrue(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        



        // Actualizar último login
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
        
        // Generar token
        //String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        
        // Obtener todos los coches para descarga inicial
        
        List<Car> allCars = carRepository.findAll();
        List<CarDTO> carDTOs = allCars.stream()
                .map(this::convertToCarDTO)
                .collect(Collectors.toList());
        
        return new LoginResponse(token, convertToUserDTO(user), carDTOs);
    }
    
    /**
     * Crear nuevo usuario (solo ADMIN)
     */
    public UserDTO createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya existe");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(User.UserRole.valueOf(request.getRole()));
        user.setActive(true);
        
        User savedUser = userRepository.save(user);
        log.info("Usuario creado: {} con rol {}", request.getEmail(), request.getRole());
        
        return convertToUserDTO(savedUser);
    }
    
    /**
     * Obtener usuario por ID
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertToUserDTO(user);
    }
    
    /**
     * Listar todos los usuarios activos
     */
    public List<UserDTO> getAllActiveUsers() {
        return userRepository.findByActiveTrue().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Actualizar usuario
     */
    public UserDTO updateUser(Long id, CreateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setFullName(request.getFullName());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Usuario actualizado: {}", id);
        
        return convertToUserDTO(updatedUser);
    }
    
    /**
     * Desactivar usuario
     */
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setActive(false);
        userRepository.save(user);
        log.info("Usuario desactivado: {}", id);
    }
    
    /**
     * Convertir User a UserDTO
     */
    private UserDTO convertToUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                user.getActive(),
                user.getCreatedAt(),
                user.getLastLogin()
        );
    }
}
