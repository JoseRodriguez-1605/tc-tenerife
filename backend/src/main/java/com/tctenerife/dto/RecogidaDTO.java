package com.tctenerife.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
 
/**
 * DTO para representar una recogida de vehículo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecogidaDTO {
    private Integer id;
    private Integer registro;
    private String empresa;
    private String vin;
    private String marca;
    private String modelo;
    private String buque;
    private String matricula;
    private LocalDateTime fechaHora;
    private Integer idBarco;
    private String estado; // ENTREGADO, DEVUELTO
    private String usuarioEmail;
}