package com.alvaro.ciudades_api.repository;

import com.alvaro.ciudades_api.entity.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
/**
 * Repositorio JPA para la entidad Ciudad. Proporciona métodos CRUD y consultas personalizadas para gestionar ciudades en la base de datos.
 * Extiende JpaRepository, lo que permite utilizar métodos predefinidos para operaciones comunes como findAll, findById, save, deleteById, entre otros.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
}
