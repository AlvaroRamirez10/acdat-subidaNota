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
}
