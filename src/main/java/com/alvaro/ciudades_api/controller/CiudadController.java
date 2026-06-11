package com.alvaro.ciudades_api.controller;

import com.alvaro.ciudades_api.entity.Ciudad;
import com.alvaro.ciudades_api.service.CiudadService;
import com.alvaro.ciudades_api.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // GET /api/ciudades — obtener todas
    @GetMapping
    public ResponseEntity<List<Ciudad>> obtenerTodas() {
        return ResponseEntity.ok(ciudadService.obtenerTodas());
    }

    // GET /api/ciudades/{id} — obtener una por ID
    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> obtenerPorId(@PathVariable Long id) {
        return ciudadService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/ciudades — crear nueva
    @PostMapping
    public ResponseEntity<Ciudad> crear(@Valid @RequestBody Ciudad ciudad) {
        Ciudad nueva = ciudadService.crear(ciudad);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // PUT /api/ciudades/{id} — actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizar(@PathVariable Long id, @Valid @RequestBody Ciudad ciudad) {
        return ciudadService.actualizar(id, ciudad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/ciudades/{id} — eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (ciudadService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/ciudades/tiempo?ciudad=Zaragoza — tiempo de cualquier ciudad
    @GetMapping("/tiempo")
    public ResponseEntity<?> obtenerTiempoPorNombre(@RequestParam String ciudad) {
        return weatherService.obtenerTiempo(ciudad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .build());
    }

    // GET /api/ciudades/{id}/tiempo — ciudad + tiempo actual
    @GetMapping("/{id}/tiempo")
    public ResponseEntity<?> obtenerCiudadConTiempo(@PathVariable Long id) {
        return ciudadService.obtenerPorId(id)
                .map(ciudad -> {
                    Map<String, Object> respuesta = new java.util.HashMap<>();
                    respuesta.put("ciudad", ciudad);
                    respuesta.put("tiempo", weatherService.obtenerTiempo(ciudad.getNombre())
                            .orElse(null));
                    return ResponseEntity.ok(respuesta);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
