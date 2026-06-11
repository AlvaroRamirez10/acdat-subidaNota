package com.alvaro.ciudades_api.controller;

import com.alvaro.ciudades_api.entity.Monumento;
import com.alvaro.ciudades_api.service.MonumentoService;
import com.alvaro.ciudades_api.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monumentos")
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
                    Map<String, Object> respuesta = new HashMap<>();
                    respuesta.put("monumento", monumento);
                    respuesta.put("tiempo", weatherService.obtenerTiempo(monumento.getCiudad().getNombre())
                            .orElse(null));
                    return (ResponseEntity<?>) ResponseEntity.ok(respuesta);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/monumentos/{ciudadId} — crear monumento en una ciudad
    @PostMapping("/{ciudadId}")
    public ResponseEntity<?> crear(@PathVariable Long ciudadId, @Valid @RequestBody Monumento monumento) {
        return monumentoService.crear(ciudadId, monumento)
                .map(nuevo -> (ResponseEntity<?>) ResponseEntity.status(HttpStatus.CREATED).body(nuevo))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/monumentos/{id} — actualizar monumento
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody Monumento monumento) {
        return monumentoService.actualizar(id, monumento)
                .map(actualizado -> (ResponseEntity<?>) ResponseEntity.ok(actualizado))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /api/monumentos/{id} — eliminar monumento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminado = monumentoService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
