package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="zona")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="ZonaGenerator")
    @SequenceGenerator(name="ZonaGenerator", sequenceName = "zona_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "tipo_zona")
    private String tipoZona;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "zonaProgramacion")
    private Set<Estacion> zonaRecords= new HashSet<Estacion>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "zonaUsuario")
    private Set<Estacion> zonaUsuariosRecords= new HashSet<Estacion>(0);


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

    public Set<Estacion> getZonaRecords() {
        return zonaRecords;
    }

    public void setZonaRecords(Set<Estacion> zonaRecords) {
        this.zonaRecords = zonaRecords;
    }

    public Set<Estacion> getZonaUsuariosRecords() {
        return zonaUsuariosRecords;
    }

    public void setZonaUsuariosRecords(Set<Estacion> zonaUsuariosRecords) {
        this.zonaUsuariosRecords = zonaUsuariosRecords;
    }

    public String toString() {
        return nombre;
    }

    public String getTipoZona() {
        return tipoZona;
    }

    public void setTipoZona(String tipoZona) {
        this.tipoZona = tipoZona;
    }

}
