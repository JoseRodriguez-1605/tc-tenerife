package com.tctenerife.service;

import com.tctenerife.dto.EstadisticasDTO;
import com.tctenerife.entity.ControlRecogida;
import com.tctenerife.repository.ControlRecogidaRepository;
import com.tctenerife.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para obtener estadísticas del dashboard.
 */
@Service
@Slf4j
public class EstadisticasService {

    @Autowired
    private ControlRecogidaRepository recogidaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public EstadisticasDTO getEstadisticas() {
        Long totalRecogidas = recogidaRepository.countTotal();
        Long recogidaHoy = recogidaRepository.countRegistrosHoy();
        Long entregados = recogidaRepository.countByEstado(ControlRecogida.Estado.ENTREGADO);
        Long devueltos = recogidaRepository.countByEstado(ControlRecogida.Estado.DEVUELTO);
        Long totalUsuarios = usuarioRepository.count();
        Long usuariosActivos = usuarioRepository.countActivosTrue();

        List<EstadisticasDTO.PorEmpresa> porEmpresa = recogidaRepository.getEstadisticasPorEmpresa().stream()
                .map(row -> new EstadisticasDTO.PorEmpresa(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .collect(Collectors.toList());

        List<EstadisticasDTO.PorUsuario> porUsuario = recogidaRepository.getEstadisticasPorUsuario().stream()
                .map(row -> new EstadisticasDTO.PorUsuario(
                        row[0] != null ? (String) row[0] : "Desconocido",
                        ((Number) row[1]).longValue()
                ))
                .collect(Collectors.toList());

        return new EstadisticasDTO(
                totalRecogidas,
                recogidaHoy,
                totalUsuarios,
                usuariosActivos,
                entregados,
                devueltos,
                porEmpresa,
                porUsuario
        );
    }
}
