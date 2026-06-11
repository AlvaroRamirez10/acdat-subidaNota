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

    // GET /api/monumentos — obtener todos
    @GetMapping
    public ResponseEntity<List<Monumento>> obtenerTodos() {
        return ResponseEntity.ok(monumentoService.obtenerTodos());
    }

    // GET /api/monumentos/{id} — obtener monumento + ciudad + tiempo
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

    // POST /api/monumentos/{ciudadId} — crear monumento en una ciudad
    @PostMapping("/{ciudadId}")
    public ResponseEntity<?> crear(@PathVariable Long ciudadId, @Valid @RequestBody Monumento monumento) {
        return monumentoService.crear(ciudadId, monumento)
                .<ResponseEntity<?>>map(nuevo -> ResponseEntity.status(HttpStatus.CREATED).body(nuevo))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/monumentos/{id} — actualizar monumento
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Monumento monumento) {
        return monumentoService.actualizar(id, monumento)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/monumentos/{id} — eliminar monumento
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