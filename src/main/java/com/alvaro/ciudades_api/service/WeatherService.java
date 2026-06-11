package com.alvaro.ciudades_api.service;

import com.alvaro.ciudades_api.dto.TiempoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

/**
 * Servicio para obtener información meteorológica de una ciudad utilizando la API de OpenWeatherMap.
 * Proporciona un método para obtener el tiempo actual de una ciudad, que devuelve un Optional con un DTO de tiempo o un Optional vacío si ocurre un error o la ciudad no se encuentra.
 * Utiliza RestTemplate para realizar la solicitud HTTP a la API externa y extraer los datos relevantes para construir el DTO de tiempo.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class WeatherService {

    @Value("${openweather.api.key}")
    private String apiKey;

    @Value("${openweather.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Obtiene la información meteorológica actual de una ciudad dada su nombre. Realiza una solicitud a la API de OpenWeatherMap y extrae los datos relevantes para construir un DTO de tiempo. Si ocurre un error durante la solicitud o la ciudad no se encuentra, retorna un Optional vacío.
     * @param nombreCiudad El nombre de la ciudad para la cual se desea obtener el tiempo.
     * @return Un Optional que contiene el DTO de tiempo si se obtiene correctamente, o un Optional vacío si ocurre un error o la ciudad no se encuentra.
     */
    public Optional<TiempoDTO> obtenerTiempo(String nombreCiudad) {
        try {
            String url = apiUrl + "?q=" + nombreCiudad + "&appid=" + apiKey + "&units=metric&lang=es";

            Map respuesta = restTemplate.getForObject(url, Map.class);

            if (respuesta == null) return Optional.empty();

            Map main = (Map) respuesta.get("main");
            Map wind = (Map) respuesta.get("wind");
            Map sys = (Map) respuesta.get("sys");
            List weather = (List) respuesta.get("weather");
            Map weatherInfo = (Map) weather.get(0);

            TiempoDTO dto = new TiempoDTO();
            dto.setCiudad(nombreCiudad);
            dto.setPais((String) sys.get("country"));
            dto.setDescripcion((String) weatherInfo.get("description"));
            dto.setTemperatura(((Number) main.get("temp")).doubleValue());
            dto.setSensacionTermica(((Number) main.get("feels_like")).doubleValue());
            dto.setHumedad(((Number) main.get("humidity")).intValue());
            dto.setViento(((Number) wind.get("speed")).doubleValue());

            return Optional.of(dto);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}