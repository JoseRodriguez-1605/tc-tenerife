package com.tctenerife.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Datos que envía el frontend para crear o actualizar un coche

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {
    private String vin;
    private String make;
    private String model;
    private String company;
}

