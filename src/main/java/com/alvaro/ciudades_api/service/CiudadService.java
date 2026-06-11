package com.alvaro.ciudades_api.service;

import com.alvaro.ciudades_api.entity.Ciudad;
import com.alvaro.ciudades_api.repository.CiudadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
/**
 * Servicio para gestionar operaciones relacionadas con la entidad Ciudad. Proporciona métodos para obtener todas las ciudades, obtener una ciudad por ID, crear una nueva ciudad, actualizar una ciudad existente y eliminar una ciudad por ID.
 * Utiliza CiudadRepository para interactuar con la base de datos y realizar las operaciones CRUD necesarias
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class CiudadService {

    /** Repositorio para la entidad Ciudad, utilizado para realizar operaciones CRUD en la base de datos. Inyectado a través del constructor para facilitar las pruebas unitarias y promover la inyección de dependencias. */
    private final CiudadRepository ciudadRepository;

    /** Constructor para inyectar el repositorio de Ciudad. Permite la creación de una instancia de CiudadService con el repositorio necesario para realizar las operaciones CRUD. */
    public CiudadService(CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
    }
/**  * Obtiene una lista de todas las ciudades almacenadas en la base de datos. Utiliza el método findAll del repositorio para recuperar todas las entidades Ciudad y devolverlas como una lista.
     * @return Lista de todas las ciudades.
     */
    public List<Ciudad> obtenerTodas() {
        return ciudadRepository.findAll();
    }

/**   * Obtiene una ciudad por su ID. Utiliza el método findById del repositorio para buscar una entidad Ciudad por su identificador único. Retorna un Optional que contiene la ciudad si se encuentra, o un Optional vacío si no se encuentra.
     * @param id Identificador de la ciudad a buscar.
     * @return Optional que contiene la ciudad encontrada o vacío si no se encuentra.
     */

    public Optional<Ciudad> obtenerPorId(Long id) {
        return ciudadRepository.findById(id);
    }

    /**
     * Crea una nueva ciudad. Utiliza el método save del repositorio para guardar una nueva entidad Ciudad en la base de datos. Retorna la ciudad creada con su ID generado.
     * @param ciudad Objeto Ciudad que se desea crear.
     * @return La ciudad creada con su ID generado.
     */
    public Ciudad crear(Ciudad ciudad) {
        return ciudadRepository.save(ciudad);
    }

    /**
     * Actualiza una ciudad existente. Retorna la ciudad actualizada si se encontró, o un Optional vacío si no se encontró.
     * @param id
     * @param ciudadNueva
     * @return
     */
    public Optional<Ciudad> actualizar(Long id, Ciudad ciudadNueva) {
        return ciudadRepository.findById(id).map(ciudad -> {
            ciudad.setNombre(ciudadNueva.getNombre());
            ciudad.setPais(ciudadNueva.getPais());
            ciudad.setPoblacion(ciudadNueva.getPoblacion());
            ciudad.setDescripcion(ciudadNueva.getDescripcion());
            return ciudadRepository.save(ciudad);
        });
    }

    /**
     * Elimina una ciudad por su ID. Retorna true si la ciudad fue eliminada, false si no se encontró.
     * @param id
     * @return
     */
    public boolean eliminar(Long id) {
        if (ciudadRepository.existsById(id)) {
            ciudadRepository.deleteById(id);
            return true;
        }
        return false;
    }
}