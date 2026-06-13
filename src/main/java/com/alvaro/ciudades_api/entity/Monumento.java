package com.alvaro.ciudades_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
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
     * * SE TRATA DE UN CAMPO SEGURO: No tiene anotación @Setter para evitar modificaciones accidentales o maliciosas.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del monumento. No puede estar vacío y es un campo obligatorio en la base de datos. */
    @Setter
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    /** Descripción opcional del monumento. Puede contener información adicional sobre el monumento, como su historia,
     * arquitectura, importancia cultural, etc. No es un campo obligatorio en la base de datos. */
    @Setter
    @Column
    private String descripcion;

    /** Relación ManyToOne con la entidad Ciudad. Cada monumento pertenece a una ciudad... */
    @Setter
    @ManyToOne
    @JoinColumn(name = "ciudad_id", nullable = false)
    @JsonBackReference
    private Ciudad ciudad;

    /** Constructor por defecto de la clase Monumento. */
    public Monumento() {
    }

    /** Constructor que acepta todos los campos de la clase Monumento, incluyendo el ID. Este constructor se utiliza
     *  principalmente para casos en los que se necesita crear un objeto Monumento con un ID específico, como al recuperar
     *  datos de la base de datos o al realizar pruebas. */
    public Monumento(Long id, String nombre, String descripcion, Ciudad ciudad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
    }

        /** Constructor que acepta todos los campos de la clase Monumento excepto el ID. Este constructor se utiliza principalmente
        * para casos en los que se necesita crear un nuevo objeto Monumento sin un ID específico, como al crear un nuevo
        * monumento antes de guardarlo en la base de datos, donde el ID se generará automáticamente. */
    public Monumento(String nombre, String descripcion, Ciudad ciudad) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
    }
}