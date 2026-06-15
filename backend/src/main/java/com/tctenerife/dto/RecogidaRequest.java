package com.tctenerife.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
/**
 * DTO para request de creación/actualización de recogida
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecogidaRequest {
    private Integer registro;
    private String empresa;
    private String vin;
    private String marca;
    private String modelo;
    private String buque;
    private String matricula;
    private Integer idBarco;
}