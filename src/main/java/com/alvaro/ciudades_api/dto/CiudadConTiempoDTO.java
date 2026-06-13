package com.alvaro.ciudades_api.dto;

import com.alvaro.ciudades_api.entity.Ciudad;
import com.alvaro.ciudades_api.entity.Monumento;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
/**
 * DTO que representa la información de una ciudad junto con su clima actual.
 * Contiene campos para el ID, nombre, país, población, descripción, lista de monumentos y un objeto ClimaDTO que
 * representa el clima actual de la ciudad.
 * Utilizado para transferir datos completos de una ciudad desde los servicios a los controladores y clientes.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class CiudadConTiempoDTO {
    private Long id;
    private String nombre;
    private String pais;
    private Integer poblacion;
    private String descripcion;
    private List<Monumento> monumentos;
    private ClimaDTO tiempo;

    /** Nuevo constructor elegante que acepta una entidad Ciudad y un ClimaDTO, y extrae automáticamente los campos necesarios
     * de la entidad para inicializar el DTO. Esto simplifica la creación del DTO a partir de la entidad y mejora la
     * legibilidad del código.
     * @param ciudad La entidad Ciudad de la cual se extraerán los datos para inicializar el DTO.
     * @param clima El objeto ClimaDTO que representa el clima actual de la ciudad.
     */
    public CiudadConTiempoDTO(Ciudad ciudad, ClimaDTO clima) {
        this(
                ciudad.getId(),
                ciudad.getNombre(),
                ciudad.getPais(),
                ciudad.getPoblacion(),
                ciudad.getDescripcion(),
                ciudad.getMonumentos(),
                clima
        );
    }
}

