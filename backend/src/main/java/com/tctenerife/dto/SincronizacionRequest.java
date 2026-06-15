package com.tctenerife.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
 
/**
 * DTO para sincronización de recogidas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SincronizacionRequest {
    private List<RecogidaData> recogidaSync;
    private Long ultimaSincronizacion;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecogidaData {
        private Integer id;
        private Integer ficha;
        private String empresa;
        private String vin;
        private String marca;
        private String modelo;
        private String buque;
        private String matricula;
        private String accion; // CREAR, ACTUALIZAR
        private Long timestamp;
    }
}