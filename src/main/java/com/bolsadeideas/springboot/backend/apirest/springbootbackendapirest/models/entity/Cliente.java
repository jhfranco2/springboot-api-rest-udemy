package com.bolsadeideas.springboot.backend.apirest.springbootbackendapirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Table(name = "clientes")
@Entity
public class Cliente implements Serializable {

    /*@PrePersist
    public void prePersist() {
        createAt = new Date();
    }*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(min = 4, max = 12,message = "El tamaño tiene que estar entre 4 y 12 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "El apellido no puede estar vacio")
    private String apellido;

    @Column(nullable = false, unique = false)
    @NotEmpty(message = "El email no puede estar vacio")
    @Email(message = "No es una dirección de correo bien formada")
    private String email;

    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "No puede estar vacia la fecha")
    private Date createAt;

    private String foto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Region region;
    
    private static final long serialVersionUID = 1L;
}
