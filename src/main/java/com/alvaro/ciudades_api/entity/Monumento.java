package com.alvaro.ciudades_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "monumentos")
/**
 * Entidad que representa un monumento en la base de datos.
 * Contiene información sobre el nombre, descripción y la ciudad a la que pertenece.
 * Utiliza anotaciones de JPA para mapear la clase a una tabla y validaciones para asegurar que los campos requeridos no
 * estén vacíos.
 * @author Álvaro
 * @version 1.0
 * @since 2026-06-11
 */
public class Monumento {

    /** Identificador único del monumento, generado automáticamente por la base de datos. Es la clave primaria de la tabla
     * "monumentos" y se utiliza para identificar de manera única cada registro en la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del monumento. No puede estar vacío y es un campo obligatorio en la base de datos. */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

        /** Descripción opcional del monumento. Puede contener información adicional sobre el monumento, como su historia,
        *  arquitectura, importancia cultural, etc. No es un campo obligatorio en la base de datos. */
    @Column
    private String descripcion;

        /** Relación ManyToOne con la entidad Ciudad. Cada monumento pertenece a una ciudad, y esta relación se establece
        * mediante una clave foránea "ciudad_id" en la tabla "monumentos". La anotación @JoinColumn especifica el nombre de
        * la columna que se utilizará para la relación y establece que no puede ser nula, lo que significa que cada
        * monumento debe estar asociado a una ciudad. */
        @ManyToOne
        @JoinColumn(name = "ciudad_id", nullable = false)
        @JsonBackReference
        private Ciudad ciudad;
}
