package com.alvaro.ciudades_api.controller;

import com.alvaro.ciudades_api.entity.Monumento;
import com.alvaro.ciudades_api.service.MonumentoService;
import com.alvaro.ciudades_api.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monumentos")
/**
 * Controlador REST para gestionar monumentos y obtener información meteorológica de su ciudad.
 * Proporciona endpoints para el CRUD de monumentos.
 * @author Álvaro
 * @version 1.1
 * @since 2026-06-11
 */
public class MonumentoController {

    private final MonumentoService monumentoService;
    private final WeatherService weatherService;

    public MonumentoController(MonumentoService monumentoService, WeatherService weatherService) {
        this.monumentoService = monumentoService;
        this.weatherService = weatherService;
    }

    /** GET /api/monumentos — obtener todos. Recupera una lista de todos los monumentos almacenados en la base de datos.
     * Devuelve un estado 200 OK con la lista de monumentos en el cuerpo de la respuesta.
     * Este endpoint es útil para permitir a los usuarios obtener una visión general de todos los monumentos disponibles
     * en la aplicación,
     * lo que puede ser útil para explorar opciones o simplemente para conocer qué monumentos están registrados.
     * @return ResponseEntity con la lista de todos los monumentos.
     */
    @GetMapping
    public ResponseEntity<List<Monumento>> obtenerTodos() {
        return ResponseEntity.ok(monumentoService.obtenerTodos());
    }

    /** GET /api/monumentos/{id} — obtener por ID. Obtiene un monumento específico utilizando su ID. Si se encuentra el
     * monumento, devuelve un estado 200 OK con el monumento en el cuerpo de la respuesta. Si no se encuentra, devuelve un
     * estado 404 Not Found.
     * Además, incluye información meteorológica de la ciudad a la que pertenece el monumento utilizando el servicio de clima.
     * La respuesta se estructura utilizando un Record para garantizar un tipado fuerte y una respuesta JSON clara.
     * @param id ID del monumento a buscar.
     * @return ResponseEntity con el monumento encontrado junto con su clima o un error 404 si no se encuentra el monumento.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return monumentoService.obtenerPorId(id)
                .map(monumento -> {
                    Object tiempo = weatherService.obtenerTiempo(monumento.getCiudad().getNombre()).orElse(null);
                    // Retorna la respuesta estructurada usando el molde estricto del Record
                    return ResponseEntity.ok(new MonumentoConTiempoResponse(monumento, tiempo));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** POST /api/monumentos/{ciudadId} — crear monumento. Crea un nuevo monumento asociado a una ciudad específica
     *  utilizando el ID de la ciudad.
     * Si el monumento se crea correctamente, devuelve un estado 201 Created con el monumento creado en el cuerpo de la
     * respuesta.
     * Si no se encuentra la ciudad con el ID proporcionado, devuelve un estado 404 Not Found.
     * @param ciudadId ID de la ciudad a la que se asociará el nuevo monumento.
     * @param monumento Objeto de la entidad Monumento que contiene los datos del nuevo monumento a crear.
     * @return ResponseEntity con el monumento creado si se crea correctamente o un error 404 si no se encuentra la ciudad.
     */
    @PostMapping("/{ciudadId}")
    public ResponseEntity<?> crear(@PathVariable Long ciudadId, @Valid @RequestBody Monumento monumento) {
        return monumentoService.crear(ciudadId, monumento)
                .<ResponseEntity<?>>map(nuevo -> ResponseEntity.status(HttpStatus.CREATED).body(nuevo))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/monumentos/{id} — actualizar monumento. Actualiza un monumento específico utilizando su ID.
     * Si el monumento se actualiza correctamente, devuelve un estado 200 OK con el monumento actualizado en el cuerpo
     * de la respuesta.
     * Si no se encuentra el monumento con el ID proporcionado, devuelve un estado 404 Not Found.
     * @param id ID del monumento a actualizar.
     * @param monumento Objeto de la entidad Monumento que contiene los datos actualizados del monumento.
     * @return ResponseEntity con el monumento actualizado si se actualiza correctamente o un error 404 si no se encuentra
     * el monumento.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Monumento monumento) {
        return monumentoService.actualizar(id, monumento)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/monumentos/{id} — eliminar monumento. Elimina un monumento específico utilizando su ID.
     * Si el monumento se elimina correctamente, devuelve un estado 204 No Content. Si no se encuentra el monumento con
     * el ID proporcionado, devuelve un estado 404 Not Found.
     * @param id
     * @return ResponseEntity con un estado 204 No Content si se elimina correctamente o un error 404 si no se encuentra
     * el monumento.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = monumentoService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Record complementario para estructurar de forma segura la respuesta JSON del monumento junto a su clima.
     * Reemplaza el uso de un HashMap genérico para garantizar un tipado fuerte en tiempo de compilación.
     * * @param monumento Objeto de la entidad Monumento que contiene la información del monumento y su ciudad.
     * @param tiempo Objeto genérico que contiene los detalles meteorológicos o null si no está disponible.
     */
    private record MonumentoConTiempoResponse(Monumento monumento, Object tiempo) {}
}