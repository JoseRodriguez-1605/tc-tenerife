package com.tctenerife.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * DTO para vehículos del catálogo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoDTO {
    private String chassis;
    private String marca;
    private String modelo;
    private String buque;
}