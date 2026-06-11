package com.alvaro.ciudades_api.service;

import com.alvaro.ciudades_api.entity.Monumento;
import com.alvaro.ciudades_api.repository.MonumentoRepository;
import com.alvaro.ciudades_api.repository.CiudadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
/**
 * Servicio que maneja la lógica de negocio relacionada con los monumentos. Proporciona métodos para obtener, crear,
 * actualizar y eliminar monumentos, así como para obtener monumentos por ciudad.
 * Utiliza los repositorios de Monumento y Ciudad para interactuar con la base de datos y realizar las operaciones
 * necesarias. Este servicio es utilizado por los controladores para gestionar las solicitudes relacionadas con los
 * monumentos.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class MonumentoService {

    private final MonumentoRepository monumentoRepository;
    private final CiudadRepository ciudadRepository;

    /** Constructor de la clase MonumentoService. Recibe los repositorios de Monumento y Ciudad como parámetros e
     * inicializa las variables correspondientes. Estos repositorios se utilizan para realizar operaciones de acceso a
     * datos relacionadas con los monumentos y las ciudades en la base de datos.
     * @param monumentoRepository Repositorio para gestionar los monumentos en la base de datos.
     * @param ciudadRepository Repositorio para gestionar las ciudades en la base de datos.
     */
    public MonumentoService(MonumentoRepository monumentoRepository, CiudadRepository ciudadRepository) {
        this.monumentoRepository = monumentoRepository;
        this.ciudadRepository = ciudadRepository;
    }

    /** Método para obtener todos los monumentos registrados en la base de datos. Utiliza el método findAll del
     * monumentoRepository para recuperar una lista de todos los monumentos disponibles.
     * @return Lista de monumentos encontrados en la base de datos.
     */
    public List<Monumento> obtenerTodos() {
        return monumentoRepository.findAll();
    }

    /** Método para obtener un monumento por su ID. Utiliza el método findById del monumentoRepository para buscar un
     * monumento específico en la base de datos. Devuelve un Optional que puede contener el monumento encontrado o
     * estar vacío si no se encuentra ningún monumento con el ID proporcionado.
     * @param id ID del monumento a buscar.
     * @return Optional que contiene el monumento encontrado o está vacío si no se encuentra ningún monumento con el ID proporcionado.
     */
    public Optional<Monumento> obtenerPorId(Long id) {
        return monumentoRepository.findById(id);
    }

    /** Método para obtener una lista de monumentos asociados a una ciudad específica. Utiliza el método findByCiudadId del
     * monumentoRepository para recuperar los monumentos que pertenecen a la ciudad con el ID proporcionado.
     * @param ciudadId ID de la ciudad para la cual se desean obtener los monumentos.
     * @return Lista de monumentos asociados a la ciudad especificada.
     */
    public List<Monumento> obtenerPorCiudad(Long ciudadId) {
        return monumentoRepository.findByCiudadId(ciudadId);
    }

    /** Método para crear un nuevo monumento asociado a una ciudad específica. Primero, verifica si la ciudad con el ID
     * proporcionado existe en la base de datos utilizando el método findById del ciudadRepository. Si la ciudad existe,
     * se establece la relación entre el monumento y la ciudad, y luego se guarda el monumento en la base de datos
     * utilizando el método save del monumentoRepository. Devuelve un Optional que contiene el monumento creado o está
     * vacío si no se encuentra la ciudad con el ID proporcionado.
     * @param ciudadId ID de la ciudad a la que se asociará el nuevo monumento.
     * @param monumento Objeto Monumento que contiene los datos del nuevo monumento a crear.
     * @return Optional que contiene el monumento creado o está vacío si no se encuentra la ciudad con el ID proporcionado.
     */
    public Optional<Monumento> crear(Long ciudadId, Monumento monumento) {
        return ciudadRepository.findById(ciudadId).map(ciudad -> {
            monumento.setCiudad(ciudad);
            return monumentoRepository.save(monumento);
        });
    }

    /** Método para actualizar un monumento existente. Primero, verifica si el monumento con el ID proporcionado existe en la
     * base de datos utilizando el método findById del monumentoRepository. Si el monumento existe, se actualizan sus
     * campos con los valores del objeto monumentoNuevo y luego se guarda el monumento actualizado en la base de datos
     * utilizando el método save del monumentoRepository. Devuelve un Optional que contiene el monumento actualizado o
     * está vacío si no se encuentra ningún monumento con el ID proporcionado.
     * @param id ID del monumento a actualizar.
     * @param monumentoNuevo Objeto Monumento que contiene los nuevos datos para actualizar el monumento existente.
     * @return Optional que contiene el monumento actualizado o está vacío si no se encuentra ningún monumento con el
     * ID proporcionado.
     */
    public Optional<Monumento> actualizar(Long id, Monumento monumentoNuevo) {
        return monumentoRepository.findById(id).map(monumento -> {
            monumento.setNombre(monumentoNuevo.getNombre());
            monumento.setDescripcion(monumentoNuevo.getDescripcion());
            return monumentoRepository.save(monumento);
        });
    }

    /** Método para eliminar un monumento por su ID. Primero, verifica si el monumento con el ID proporcionado existe en
     *  la base de
     * datos utilizando el método existsById del monumentoRepository. Si el monumento existe, se elimina de la base de
     * datos utilizando el método deleteById del monumentoRepository. Devuelve un boolean que indica si el monumento fue
     * eliminado (true) o si no se encontró ningún monumento con el ID proporcionado (false).
     * @param id ID del monumento a eliminar.
     * @return true si el monumento fue eliminado, false si no se encontró ningún monumento con el ID proporcionado.
     */
    public boolean eliminar(Long id) {
        boolean existe = monumentoRepository.existsById(id);
        if (existe) {
            monumentoRepository.deleteById(id);
        }
        return existe;
    }
}
