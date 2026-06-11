package com.alvaro.ciudades_api.controller;

import com.alvaro.ciudades_api.dto.ClimaDTO;
import com.alvaro.ciudades_api.dto.CiudadConTiempoDTO;
import com.alvaro.ciudades_api.dto.TiempoDTO;
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
 * Proporciona endpoints para CRUD de ciudades y para obtener el tiempo actual de una ciudad.
 * Utiliza CiudadService para operaciones de ciudad y WeatherService para obtener datos meteorológicos.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class CiudadController {

    /** Servicio para gestionar operaciones relacionadas con la entidad Ciudad. Inyectado a través del constructor para
     *  facilitar las pruebas unitarias y promover la inyección de dependencias. */
    private final CiudadService ciudadService;
    private final WeatherService weatherService;

    /** Constructor para inyectar los servicios necesarios. Permite la creación de una instancia de CiudadController con
     *  los servicios necesarios para gestionar ciudades y obtener información meteorológica. */
    public CiudadController(CiudadService ciudadService, WeatherService weatherService) {
        this.ciudadService = ciudadService;
        this.weatherService = weatherService;
    }

    /** GET /api/ciudades — obtener todas las ciudades
     * @return ResponseEntity con la lista de todas las ciudades. Si no hay ciudades, retorna una lista vacía.
     * El código de estado HTTP es 200 OK en caso de éxito.
     * */
    @GetMapping
    public ResponseEntity<List<Ciudad>> obtenerTodas() {
        return ResponseEntity.ok(ciudadService.obtenerTodas());
    }

    /** GET /api/ciudades/{id} — obtener por ID
     * @param id El identificador de la ciudad a obtener.
     * @return ResponseEntity con la ciudad encontrada. Si no se encuentra la ciudad, retorna un código de estado
     * HTTP 404 Not Found.
     * */
    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> obtenerPorId(@PathVariable Long id) {
        return ciudadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/ciudades — crear
     * @param ciudad El objeto Ciudad a crear. Debe ser válido según las anotaciones de validación.
     * @return ResponseEntity con la ciudad creada. El código de estado HTTP es 201 Created en caso de éxito.
     * Si el objeto Ciudad no es válido, Spring automáticamente retornará un código de estado HTTP 400 Bad Request.
     * */
    @PostMapping
    public ResponseEntity<Ciudad> crear(@Valid @RequestBody Ciudad ciudad) {
        Ciudad nueva = ciudadService.crear(ciudad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

        /** PUT /api/ciudades/{id} — actualizar
        * @param id El identificador de la ciudad a actualizar.
        * @param ciudad El objeto Ciudad con los datos actualizados. Debe ser válido según las anotaciones de validación.
        * @return ResponseEntity con la ciudad actualizada. Si se encuentra la ciudad y se actualiza correctamente, el código de estado HTTP es 200 OK.
        * Si no se encuentra la ciudad, retorna un código de estado HTTP 404 Not Found.
        * Si el objeto Ciudad no es válido, Spring automáticamente retornará un código de estado HTTP 400 Bad Request.
        * */
    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizar(@PathVariable Long id, @Valid @RequestBody Ciudad ciudad) {
        return ciudadService.actualizar(id, ciudad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        /** DELETE /api/ciudades/{id} — eliminar
        * @param id El identificador de la ciudad a eliminar.
        * @return ResponseEntity sin contenido. Si se encuentra la ciudad y se elimina correctamente, el código de estado
         * HTTP es 204 No Content.
        * Si no se encuentra la ciudad, retorna un código de estado HTTP 404 Not Found.
        * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ResponseEntity<Void> respuesta = ciudadService.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();

        return respuesta;
    }

        /** GET /api/ciudades/tiempo?ciudad={nombre} — obtener tiempo por nombre de ciudad
        * @param ciudad El nombre de la ciudad para la cual se desea obtener el tiempo.
        * @return ResponseEntity con el DTO de tiempo si se obtiene correctamente. Si ocurre un error o la ciudad no se
         * encuentra, retorna un código de estado HTTP 503 Service Unavailable.
        * */
    @GetMapping("/tiempo")
    public ResponseEntity<?> obtenerTiempoPorNombre(@RequestParam String ciudad) {
        return weatherService.obtenerTiempo(ciudad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .build());
    }

    /** GET /api/ciudades/{id}/tiempo — obtener ciudad con tiempo por ID
     * @param id El identificador de la ciudad para la cual se desea obtener la información de la ciudad junto con el tiempo.
     * @return ResponseEntity con la ciudad y su información meteorológica si se encuentra la ciudad.
     * Si no se encuentra la ciudad, retorna un código de estado HTTP 404 Not Found.
     * Si ocurre un error al obtener la información meteorológica, el campo "tiempo" será null.
     * */
    @GetMapping("/{id}/tiempo")
    public ResponseEntity<CiudadConTiempoDTO> obtenerCiudadConTiempo(@PathVariable Long id) {
        ResponseEntity<CiudadConTiempoDTO> respuesta = ciudadService.obtenerPorId(id)
                .map(this::crearRespuestaCiudadConTiempo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

        return respuesta;
    }

    /** Método privado para crear una respuesta que contiene la ciudad y su información meteorológica.
     * Utiliza el servicio de clima para obtener el tiempo de la ciudad y construye un DTO con la ciudad y el tiempo.
     * Si ocurre un error al obtener la información meteorológica, el campo "tiempo" será null.
     * @param ciudad La ciudad para la cual se desea crear la respuesta con su información meteorológica.
     * @return Un DTO que contiene la ciudad y su información meteorológica.
     * */
    private CiudadConTiempoDTO crearRespuestaCiudadConTiempo(Ciudad ciudad) {
        ClimaDTO clima = weatherService.obtenerTiempo(ciudad.getNombre())
                .map(this::crearClimaDTO)
                .orElse(null);

        CiudadConTiempoDTO respuesta = new CiudadConTiempoDTO(
                ciudad.getId(),
                ciudad.getNombre(),
                ciudad.getPais(),
                ciudad.getPoblacion(),
                ciudad.getDescripcion(),
                ciudad.getMonumentos(),
                clima
        );

        return respuesta;
    }

    private ClimaDTO crearClimaDTO(TiempoDTO tiempo) {
        ClimaDTO clima = new ClimaDTO(
                tiempo.getDescripcion(),
                tiempo.getTemperatura(),
                tiempo.getSensacionTermica(),
                tiempo.getHumedad(),
                tiempo.getViento()
        );

        return clima;
    }
}
