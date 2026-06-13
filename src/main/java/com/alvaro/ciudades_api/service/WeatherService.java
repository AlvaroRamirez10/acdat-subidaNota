package com.alvaro.ciudades_api.service;

import com.alvaro.ciudades_api.dto.TiempoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

/**
 * Servicio para obtener información meteorológica de una ciudad utilizando la API de OpenWeatherMap.
 * Proporciona un método para obtener el tiempo actual de una ciudad, que devuelve un Optional con un DTO de tiempo o un
 * Optional vacío si ocurre un error o la ciudad no se encuentra.
 * Utiliza RestTemplate para realizar la solicitud HTTP a la API externa y extraer los datos relevantes para construir
 * el DTO de tiempo.
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
     * Obtiene la información meteorológica actual de una ciudad dada su nombre. Realiza una solicitud a la API de
     * OpenWeatherMap y extrae los datos relevantes para construir un DTO de tiempo. Si ocurre un error durante
     * la solicitud o la ciudad no se encuentra, retorna un Optional vacío.
     * @param nombreCiudad El nombre de la ciudad para la cual se desea obtener el tiempo.
     * @return Un Optional que contiene el DTO de tiempo si se obtiene correctamente,
     * o un Optional vacío si ocurre un error o la ciudad no se encuentra.
     */
    public Optional<TiempoDTO> obtenerTiempo(String nombreCiudad) {
        Optional<TiempoDTO> tiempo = Optional.empty();

        try {
            Map respuesta = consultarApi(nombreCiudad);

            if (respuesta != null) {
                tiempo = Optional.of(crearTiempoDTO(nombreCiudad, respuesta));
            }
        } catch (RestClientException | ClassCastException | NullPointerException | IndexOutOfBoundsException e) {
            tiempo = Optional.empty();
        }

        return tiempo;
    }

    /** Realiza una solicitud HTTP a la API de OpenWeatherMap para obtener la información meteorológica de una ciudad. Construye
     * la URL de la solicitud utilizando el nombre de la ciudad, la clave de API y otros parámetros necesarios. Retorna un
     * Map con la respuesta de la API, que contiene los datos meteorológicos, o null si ocurre un error durante la solicitud.
     * @param nombreCiudad El nombre de la ciudad para la cual se desea obtener el tiempo.
     * @return Un Map con la respuesta de la API que contiene los datos meteorológicos, o null si ocurre un error.
     */
    private Map consultarApi(String nombreCiudad) {
        String url = construirUrl(nombreCiudad);
        return restTemplate.getForObject(url, Map.class);
    }

    /** Construye la URL para la solicitud a la API de OpenWeatherMap utilizando el nombre de la ciudad, la clave de API
     *  y otros
     * parámetros necesarios. La URL incluye el nombre de la ciudad como parámetro de consulta, la clave de API
     * para autenticación,
     * y parámetros adicionales para obtener los datos en unidades métricas y en español.
     * @param nombreCiudad El nombre de la ciudad para la cual se desea obtener el tiempo.
     * @return La URL construida para realizar la solicitud a la API de OpenWeatherMap.
     */
    private String construirUrl(String nombreCiudad) {
        return apiUrl + "?q=" + nombreCiudad + "&appid=" + apiKey + "&units=metric&lang=es";
    }

    /** Crea un DTO de tiempo a partir de la respuesta obtenida de la API de OpenWeatherMap. Extrae los datos relevantes
     *  de la respuesta, como la temperatura, la sensación térmica, la humedad, la velocidad del viento, la descripción
     *  del clima y el país. Utiliza estos datos para construir un objeto TiempoDTO que representa la información
     *  meteorológica de la ciudad.
     * @param nombreCiudad El nombre de la ciudad para la cual se desea crear el DTO de tiempo.
     * @param respuesta Un Map que contiene la respuesta de la API de OpenWeatherMap con los datos meteorológicos.
     * @return Un objeto TiempoDTO que representa la información meteorológica de la ciudad.
     */
    private TiempoDTO crearTiempoDTO(String nombreCiudad, Map respuesta) {
        //Map para extraer los datos relevantes de la respuesta de la API, como la temperatura, la sensación térmica,
        // la humedad, la velocidad del viento, la descripción
        Map main = (Map) respuesta.get("main");
        Map wind = (Map) respuesta.get("wind");
        Map sys = (Map) respuesta.get("sys");
        List weather = (List) respuesta.get("weather");
        Map weatherInfo = (Map) weather.get(0);

        //Construye el DTO de tiempo utilizando los datos extraídos de la respuesta de la API. Asigna el nombre de la ciudad,
        // el país, la descripción del clima, la temperatura,
        TiempoDTO dto = new TiempoDTO();
        dto.setCiudad(nombreCiudad);
        dto.setPais((String) sys.get("country"));
        dto.setDescripcion((String) weatherInfo.get("description"));
        dto.setTemperatura(((Number) main.get("temp")).doubleValue());
        dto.setSensacionTermica(((Number) main.get("feels_like")).doubleValue());
        dto.setHumedad(((Number) main.get("humidity")).intValue());
        dto.setViento(((Number) wind.get("speed")).doubleValue());

        return dto;
    }
}
