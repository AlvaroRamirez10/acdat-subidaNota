package com.alvaro.ciudades_api.dto;

import lombok.Data;

@Data

/**
 * DTO que representa la información del tiempo para una ciudad.
 * Contiene campos para la ciudad, país, descripción del clima, temperatura, sensación térmica, humedad y velocidad del viento.
 * Utilizado para transferir datos meteorológicos desde el servicio de clima a los controladores y clientes.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class TiempoDTO {
    private String ciudad;
    private String pais;
    private String descripcion;
    private double temperatura;
    private double sensacionTermica;
    private int humedad;
    private double viento;
}
