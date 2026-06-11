package com.alvaro.ciudades_api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
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
     * * SE TRATA DE UN CAMPO SEGURO: No tiene anotación @Setter para evitar modificaciones accidentales o maliciosas.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la ciudad. No puede estar vacío y es un campo obligatorio en la base de datos. */
    @Setter
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String nombre;

    /** País al que pertenece la ciudad. No puede estar vacío y es un campo obligatorio en la base de datos. */
    @Setter
    @NotBlank(message = "El país no puede estar vacío")
    @Column(nullable = false)
    private String pais;

    /** Población de la ciudad. No puede ser nula y es un campo obligatorio en la base de datos. */
    @Setter
    @NotNull(message = "La población no puede ser nula")
    @Column(nullable = false)
    private Integer poblacion;

    /** Descripción opcional de la ciudad. Puede contener información adicional sobre la ciudad, como su historia,
     * cultura, atracciones turísticas, etc. No es un campo obligatorio en la base de datos. */
    @Setter
    @Column
    private String descripcion;

    /** Relación OneToMany con la entidad Monumento. Una ciudad puede tener múltiples monumentos asociados... */
    @Setter
    @OneToMany(mappedBy = "ciudad", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Monumento> monumentos;

    public Ciudad() {
    }

    public Ciudad(String nombre, String pais, List<Monumento> monumentos, String descripcion, Integer poblacion) {
        this.nombre = nombre;
        this.pais = pais;
        this.monumentos = monumentos;
        this.descripcion = descripcion;
        this.poblacion = poblacion;
    }

    public Ciudad(Long id, String nombre, String pais, Integer poblacion, String descripcion,
                  List<Monumento> monumentos) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.poblacion = poblacion;
        this.descripcion = descripcion;
        this.monumentos = monumentos;
    }
}