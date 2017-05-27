package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tabla_maestra")
public class TablaMaestra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TablaGenerator")
    @SequenceGenerator(name="TablaGenerator", sequenceName = "tabla_maestra_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "fecha_vigencia")
    private Date fechaVigencia;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo_dia")
    private String tipoDia;

    @Column(name = "es_definitiva")
    private Boolean esDefinitiva;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "matriz_distancia", nullable = false)
    private MatrizDistancia matrizDistancia;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "gis_carga", nullable = false)
    private GisCarga gisCarga;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tablaMeestra", cascade = CascadeType.ALL)
    private Set<TablaMaestraServicios> tablaServiciosRecords = new HashSet<TablaMaestraServicios>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tablaMaestra", cascade = CascadeType.ALL)
    private Set<GisIntervalos> gisIntervaloses = new HashSet<GisIntervalos>(0);

    @Transient
    private String fechaCreacionFormato;
    @Transient
    private String fechaProgramacionFormato;

    public TablaMaestra() {
    }

    public TablaMaestra(Date fechaCreacion, Date fechaVigencia, String descripcion, MatrizDistancia matrizDistancia, GisCarga gisCarga) {
        this.fechaCreacion = fechaCreacion;
        this.fechaVigencia = fechaVigencia;
        this.descripcion = descripcion;
        this.matrizDistancia = matrizDistancia;
        this.gisCarga = gisCarga;
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

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public MatrizDistancia getMatrizDistancia() {
        return matrizDistancia;
    }

    public void setMatrizDistancia(MatrizDistancia matrizDistancia) {
        this.matrizDistancia = matrizDistancia;
    }

    public GisCarga getGisCarga() {
        return gisCarga;
    }

    public void setGisCarga(GisCarga gisCarga) {
        this.gisCarga = gisCarga;
    }

    public Set<TablaMaestraServicios> getTablaServiciosRecords() {
        return tablaServiciosRecords;
    }

    public void setTablaServiciosRecords(Set<TablaMaestraServicios> tablaServiciosRecords) {
        this.tablaServiciosRecords = tablaServiciosRecords;
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
        return dt1.format(fechaVigencia);
    }

    public void setFechaProgramacionFormato(String fechaProgramacionFormato) {
        this.fechaProgramacionFormato = fechaProgramacionFormato;
    }

    public Set<GisIntervalos> getGisIntervaloses() {
        return gisIntervaloses;
    }

    public void setGisIntervaloses(Set<GisIntervalos> gisIntervaloses) {
        this.gisIntervaloses = gisIntervaloses;
    }

    public Boolean getEsDefinitiva() {
        return esDefinitiva;
    }

    public void setEsDefinitiva(Boolean esDefinitiva) {
        this.esDefinitiva = esDefinitiva;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }
}
