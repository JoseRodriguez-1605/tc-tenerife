// backend/src/main/java/com/tctenerife/dto/EstadisticasDTO.java
package com.tctenerife.dto;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
 
/**
 * DTO para estadísticas del dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasDTO {
    private Long totalRecogidas;
    private Long recogidaHoy;
    private Long totalUsuarios;
    private Long usuariosActivos;
    private Long entregados;
    private Long devueltos;
    private List<PorEmpresa> porEmpresa;
    private List<PorUsuario> porUsuario;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PorEmpresa {
        private String empresa;
        private Long cantidad;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PorUsuario {
        private String usuario;
        private Long cantidad;
    }
}