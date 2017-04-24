package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="estacion")
public class Estacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="EstacionGenerator")
    @SequenceGenerator(name="EstacionGenerator", sequenceName = "estacion_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo")
    private Integer codigo;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zona_programacion", nullable = false)
    private Zona zonaProgramacion;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zona_usuario", nullable = false)
    private Zona zonaUsuario;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "estacion", cascade = CascadeType.REMOVE)
    private Set<Vagon> estacionRecords= new HashSet<Vagon>(0);


    public Estacion() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Zona getZonaProgramacion() {
        return zonaProgramacion;
    }

    public void setZonaProgramacion(Zona zonaProgramacion) {
        this.zonaProgramacion = zonaProgramacion;
    }

    public Zona getZonaUsuario() {
        return zonaUsuario;
    }

    public void setZonaUsuario(Zona zonaUsuario) {
        this.zonaUsuario = zonaUsuario;
    }

    public Set<Vagon> getEstacionRecords() {
        return estacionRecords;
    }

    public void setEstacionRecords(Set<Vagon> estacionRecords) {
        this.estacionRecords = estacionRecords;
    }
}
