package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
// Representa un coche para enviar al frontend

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
    private Long id;
    private String vin;
    private String make;
    private String model;
    private String company;
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt;
    private String createdByUserEmail; //Crea un gmail para el usuario.
    private String updatedByUserEmail;  // Actualiza el usuario.
    private Boolean isSynced;
}
