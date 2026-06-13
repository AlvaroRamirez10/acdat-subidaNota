package com.alvaro.ciudades_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO que representa la información del clima para una ciudad.
 * Contiene campos para la descripción del clima, temperatura, sensación térmica, humedad y velocidad del viento.
 * Utilizado para transferir datos meteorológicos desde el servicio de clima a los controladores y clientes.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class ClimaDTO {
    private String descripcion;
    private double temperatura;
    private double sensacionTermica;
    private int humedad;
    private double viento;

    /** Nuevo constructor elegante que acepta un objeto TiempoDTO y extrae automáticamente los campos necesarios para
     * inicializar el ClimaDTO. Esto simplifica la creación del ClimaDTO a partir del TiempoDTO y mejora la legibilidad del código.
     * @param tiempo El objeto TiempoDTO del cual se extraerán los datos para inicializar el ClimaDTO.
     */
    public ClimaDTO(TiempoDTO tiempo) {
        this(
                tiempo.getDescripcion(),
                tiempo.getTemperatura(),
                tiempo.getSensacionTermica(),
                tiempo.getHumedad(),
                tiempo.getViento()
        );
    }
}
