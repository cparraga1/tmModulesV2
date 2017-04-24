package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="matriz_distancia")
public class MatrizDistancia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="MatrizGenerator")
    @SequenceGenerator(name="MatrizGenerator", sequenceName = "matriz_distancia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "fecha_habil")
    private Date fechaAplicacion;

    @Column(name = "fecha_sabado")
    private Date fechaSabado;

    @Column(name = "fecha_festivo")
    private Date fechaFestivo;

    @Column(name = "numeracion")
    private String numeracion;


    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "matrizDistancia")
    private Set<DistanciaNodos> distanciaNodosRecords= new HashSet<DistanciaNodos>(0);


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "matrizDistancia")
    private Set<TablaMaestra> tablaMaestraRecords= new HashSet<TablaMaestra>(0);

    @Transient
    private String fechaCreacionFormato;
    @Transient
    private String fechaProgramacionFormato;

    public MatrizDistancia() {
    }

    public MatrizDistancia(Date fechaCreacion, Date fechaAplicacion, Date fechaSabado, Date fechaFestivo, String numeracion, String descripcion) {
        this.fechaCreacion = fechaCreacion;
        this.fechaAplicacion = fechaAplicacion;
        this.fechaSabado = fechaSabado;
        this.fechaFestivo = fechaFestivo;
        this.numeracion = numeracion;
        this.descripcion = descripcion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public Set<DistanciaNodos> getDistanciaNodosRecords() {
        return distanciaNodosRecords;
    }

    public void setDistanciaNodosRecords(Set<DistanciaNodos> distanciaNodosRecords) {
        this.distanciaNodosRecords = distanciaNodosRecords;
    }

    public String getFechaCreacionFormato() {
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        return dt1.format(fechaCreacion);
    }

    public void setFechaCreacionFormato(String fechaCreacionFormato) {
        this.fechaCreacionFormato = fechaCreacionFormato;
    }

    public String getFechaProgramacionFormato() {
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        return dt1.format(fechaAplicacion);
    }

    public void setFechaProgramacionFormato(String fechaProgramacionFormato) {
        this.fechaProgramacionFormato = fechaProgramacionFormato;
    }

    public Set<TablaMaestra> getTablaMaestraRecords() {
        return tablaMaestraRecords;
    }

    public void setTablaMaestraRecords(Set<TablaMaestra> tablaMaestraRecords) {
        this.tablaMaestraRecords = tablaMaestraRecords;
    }

    public Date getFechaSabado() {
        return fechaSabado;
    }

    public void setFechaSabado(Date fechaSabado) {
        this.fechaSabado = fechaSabado;
    }

    public Date getFechaFestivo() {
        return fechaFestivo;
    }

    public void setFechaFestivo(Date fechaFestivo) {
        this.fechaFestivo = fechaFestivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
