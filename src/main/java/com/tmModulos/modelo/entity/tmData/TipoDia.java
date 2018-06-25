package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tipodia")
public class TipoDia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TipoDiaGenerator")
    @SequenceGenerator(name="TipoDiaGenerator", sequenceName = "tipodia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    public TipoDia() {
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDia")
    private Set<TipoDiaDetalle> tipoDiaDetalleRecords = new HashSet<TipoDiaDetalle>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDia")
    private Set<ServicioTipoDia> servicioTipoDiaRecords = new HashSet<ServicioTipoDia>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDia")
    private Set<GisIntervalos> gisIntervaloses = new HashSet<GisIntervalos>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDia")
    private Set<GisCarga> gisCarga = new HashSet<GisCarga>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDia")
    private Set<Horario> horario = new HashSet<Horario>(0);

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

    public Set<TipoDiaDetalle> getTipoDiaDetalleRecords() {
        return tipoDiaDetalleRecords;
    }

    public void setTipoDiaDetalleRecords(Set<TipoDiaDetalle> tipoDiaDetalleRecords) {
        this.tipoDiaDetalleRecords = tipoDiaDetalleRecords;
    }

    public Set<ServicioTipoDia> getServicioTipoDiaRecords() {
        return servicioTipoDiaRecords;
    }

    public void setServicioTipoDiaRecords(Set<ServicioTipoDia> servicioTipoDiaRecords) {
        this.servicioTipoDiaRecords = servicioTipoDiaRecords;
    }

    public Set<GisIntervalos> getGisIntervaloses() {
        return gisIntervaloses;
    }

    public void setGisIntervaloses(Set<GisIntervalos> gisIntervaloses) {
        this.gisIntervaloses = gisIntervaloses;
    }

    public Set<GisCarga> getGisCarga() {
        return gisCarga;
    }

    public void setGisCarga(Set<GisCarga> gisCarga) {
        this.gisCarga = gisCarga;
    }

    public Set<Horario> getHorario() {
        return horario;
    }

    public void setHorario(Set<Horario> horario) {
        this.horario = horario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
