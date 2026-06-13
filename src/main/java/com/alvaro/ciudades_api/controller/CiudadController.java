package com.alvaro.ciudades_api.controller;

import com.alvaro.ciudades_api.dto.ClimaDTO;
import com.alvaro.ciudades_api.dto.CiudadConTiempoDTO;
import com.alvaro.ciudades_api.entity.Ciudad;
import com.alvaro.ciudades_api.service.CiudadService;
import com.alvaro.ciudades_api.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
/**
 * Controlador REST para gestionar ciudades y obtener información meteorológica.
 * @author Álvaro
 * @version 1.1
 * @since 2026-06-11
 */
public class CiudadController {

    private final CiudadService ciudadService;
    private final WeatherService weatherService;

    /** Constructor para inyectar las dependencias de CiudadService y WeatherService.
     * @param ciudadService Servicio para gestionar operaciones relacionadas con ciudades.
     * @param weatherService Servicio para obtener información meteorológica.
     */
    public CiudadController(CiudadService ciudadService, WeatherService weatherService) {
        this.ciudadService = ciudadService;
        this.weatherService = weatherService;
    }

    /** Endpoint para obtener todas las ciudades. Utiliza el servicio de ciudades para recuperar la lista completa de ciudades
     * almacenadas en la base de datos. Devuelve un estado 200 OK con la lista de ciudades en el cuerpo de la respuesta.
     * Este endpoint es útil para permitir a los usuarios obtener una visión general de todas las ciudades disponibles en la aplicación,
     * lo que puede ser útil para explorar opciones o simplemente para conocer qué ciudades están registradas.
     * @return ResponseEntity con la lista de todas las ciudades.
     */
    @GetMapping
    public ResponseEntity<List<Ciudad>> obtenerTodas() {
        return ResponseEntity.ok(ciudadService.obtenerTodas());
    }

    /** Endpoint para obtener una ciudad por su ID. Utiliza el servicio de ciudades para buscar la ciudad especificada.
     * Si se encuentra la ciudad, devuelve un estado 200 OK con la ciudad en el cuerpo de la respuesta. Si no se encuentra,
     * devuelve un estado 404 Not Found.
     * Este endpoint es útil para permitir a los usuarios obtener información detallada sobre una ciudad específica utilizando
     * su ID único.
     * @param id ID de la ciudad a buscar.
     * @return ResponseEntity con la ciudad encontrada o un error 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> obtenerPorId(@PathVariable Long id) {
        return ciudadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para crear una nueva ciudad. Recibe un objeto Ciudad en el cuerpo de la solicitud, lo valida y utiliza
     * el servicio de ciudades para crear la ciudad en la base de datos. Si la ciudad se crea correctamente, devuelve
     * la ciudad creada con un estado 201 Created. Si la validación falla, devuelve un error 400 Bad Request con los
     * detalles de la validación.
     * @param ciudad
     * @return ResponseEntity con la ciudad creada o un error 400 si la validación falla.
     */
    @PostMapping
    public ResponseEntity<Ciudad> crear(@Valid @RequestBody Ciudad ciudad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadService.crear(ciudad));
    }

    /**
     * Endpoint para actualizar una ciudad existente. Recibe el ID de la ciudad a actualizar y un objeto Ciudad con los
     * nuevos datos.
     * Utiliza el servicio de ciudades para realizar la actualización. Si la ciudad se actualiza correctamente, devuelve
     * la ciudad actualizada con un estado 200 OK. Si no se encuentra la ciudad con el ID proporcionado,
     * devuelve un estado 404 Not Found.
     * @param id
     * @param ciudad
     * @return ResponseEntity con la ciudad actualizada o un error 404 si no se encuentra la ciudad con el ID proporcionado.
     */

    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizar(@PathVariable Long id, @Valid @RequestBody Ciudad ciudad) {
        return ciudadService.actualizar(id, ciudad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Endpoint para eliminar una ciudad por su ID. Utiliza el servicio de ciudades para eliminar la ciudad especificada.
     *  Si la ciudad
     * se elimina correctamente, devuelve un estado 204 No Content. Si no se encuentra la ciudad con el ID proporcionado,
     * devuelve un estado 404 Not Found.
     * Este endpoint es útil para permitir a los usuarios eliminar ciudades que ya no desean mantener en la base de datos,
     * lo que ayuda a mantener la información actualizada y relevante.
     * @param id ID de la ciudad a eliminar.
     * @return ResponseEntity con el estado de la operación de eliminación.
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return ciudadService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Endpoint para obtener el clima actual de una ciudad por su nombre. Utiliza el servicio de clima para obtener la información
     * meteorológica de la ciudad especificada. Si el servicio de clima no está disponible o no se encuentra la ciudad,
     * devuelve un error 503. Si se encuentra la ciudad y el clima, devuelve la información meteorológica en formato JSON.
     * Este endpoint es útil para proporcionar a los usuarios información actualizada sobre el clima de una ciudad
     * específica, lo que puede ser útil para planificar viajes, actividades al aire libre o simplemente para conocer
     * las condiciones climáticas actuales.
     * @param ciudad
     * @return ResponseEntity con la información meteorológica de la ciudad o un error 503 si el servicio no está disponible.
     */
    @GetMapping("/tiempo")
    public ResponseEntity<?> obtenerTiempoPorNombre(@RequestParam String ciudad) {
        return weatherService.obtenerTiempo(ciudad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }

    /**
     * Endpoint para obtener la información de una ciudad junto con su clima actual. Utiliza el servicio de ciudades para
     * obtener la ciudad por ID y luego el servicio de clima para obtener el clima actual de esa ciudad.
     * La respuesta se mapea a un DTO que contiene toda la información relevante.
     * @param id
     * @return ResponseEntity con la información de la ciudad y su clima actual, o un error 404 si no se encuentra la ciudad.
     */
    @GetMapping("/{id}/tiempo")
    public ResponseEntity<CiudadConTiempoDTO> obtenerCiudadConTiempo(@PathVariable Long id) {
        return ciudadService.obtenerPorId(id)
                .map(this::crearRespuestaCiudadConTiempo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mapea de forma limpia la ciudad y su clima utilizando los nuevos constructores del DTO.
     * Este método se encarga de crear una instancia de CiudadConTiempoDTO a partir de una entidad Ciudad, obteniendo
     * el clima
     * actual de la ciudad utilizando el servicio de clima. Si el clima no está disponible, se asigna null al campo de
     * clima en el DTO.
     * @param ciudad Entidad Ciudad de la cual se desea crear el DTO con clima.
     * @return CiudadConTiempoDTO que contiene la información de la ciudad y su clima actual (o null si no está disponible
     */
    private CiudadConTiempoDTO crearRespuestaCiudadConTiempo(Ciudad ciudad) {
        ClimaDTO clima = weatherService.obtenerTiempo(ciudad.getNombre())
                .map(ClimaDTO::new) // Usa el constructor de ClimaDTO que recibe un TiempoDTO
                .orElse(null);

        return new CiudadConTiempoDTO(ciudad, clima); // Usa el constructor elegante
    }
}