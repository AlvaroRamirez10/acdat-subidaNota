package com.alvaro.ciudades_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "ciudades")
/**
 * Entidad que representa una ciudad en la base de datos.
 * Contiene información sobre el nombre, país, población y una descripción opcional.
 * Utiliza anotaciones de JPA para mapear la clase a una tabla y validaciones para asegurar que los campos requeridos no
 * estén vacíos.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class Ciudad {

    /**
     * Identificador único de la ciudad, generado automáticamente por la base de datos. Es la clave primaria de la tabla
     * "ciudades" y se utiliza para identificar de manera única cada registro en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la ciudad. No puede estar vacío y es un campo obligatorio en la base de datos. */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    /** País al que pertenece la ciudad. No puede estar vacío y es un campo obligatorio en la base de datos. */
    @NotBlank(message = "El país no puede estar vacío")
    @Column(nullable = false)
    private String pais;

     /** Población de la ciudad. No puede ser nula y es un campo obligatorio en la base de datos. */
    @NotNull(message = "La población no puede ser nula")
    @Column(nullable = false)
    private Integer poblacion;

        /** Descripción opcional de la ciudad. Puede contener información adicional sobre la ciudad, como su historia,
         *  cultura, atracciones turísticas, etc. No es un campo obligatorio en la base de datos. */
    @Column
    private String descripcion;
}