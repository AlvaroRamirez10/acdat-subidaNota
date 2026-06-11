package com.alvaro.ciudades_api.repository;

import com.alvaro.ciudades_api.entity.Monumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
/**
 * Repositorio JPA para la entidad Monumento. Proporciona métodos CRUD y consultas personalizadas para gestionar monumentos
 * en la base de datos.
 * Extiende JpaRepository, lo que permite utilizar métodos predefinidos para operaciones comunes como findAll, findById,
 * save, deleteById, entre otros. Además, incluye un método personalizado para encontrar monumentos por el ID de la
 * ciudad a la que pertenecen.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public interface MonumentoRepository extends JpaRepository<Monumento, Long> {
    //Método personalizado para encontrar monumentos por el ID de la ciudad a la que pertenecen. Este método utiliza la
    //convención de nomenclatura de Spring Data JPA para generar automáticamente la consulta SQL necesaria para
    // recuperar los monumentos asociados a una ciudad específica.
    List<Monumento> findByCiudadId(Long ciudadId);
}
