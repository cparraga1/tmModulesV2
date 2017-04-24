package com.tmModulos.modelo.entity.tmData;


import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="giscarga")
public class GisCarga {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GisGenerator")
    @SequenceGenerator(name="GisGenerator", sequenceName = "giscarga_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
    @Column(name = "fecha_programacion")
    private Date fechaProgramacion;
    @Column(name = "fecha_vigencia")
    private Date fechaVigencia;
    @Column(name = "esta_vigente")
    private boolean estaVigente;
    @Column(name = "descripcion")
    private String descripcion;
    @Transient
    private String fechaCreacionFormato;
    @Transient
    private String fechaProgramacionFormato;
    @Transient
    private String fechaVigenciaFormato;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_dia", nullable = false)
    private TipoDia tipoDia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gisCargaArco")
    private Set<ArcoTiempo> arcoTiempoRecords = new HashSet<ArcoTiempo>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "gisCarga")
    private Set<TablaMaestra> tablaMaestraRecords= new HashSet<TablaMaestra>(0);

    public GisCarga(Date fechaCreacion, Date fechaProgramacion, Date fechaVigencia, String descripcion,TipoDia tipoDia) {
        this.fechaCreacion = fechaCreacion;
        this.fechaProgramacion = fechaProgramacion;
        this.fechaVigencia = fechaVigencia;
        this.descripcion = descripcion;
        this.estaVigente = false;
        this.tipoDia =tipoDia;
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
    }

    public GisCarga() {
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

    public Date getFechaProgramacion() {
        return fechaProgramacion;
    }

    public void setFechaProgramacion(Date fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public boolean isEstaVigente() {
        return estaVigente;
    }

    public void setEstaVigente(boolean estaVigente) {
        this.estaVigente = estaVigente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<ArcoTiempo> getArcoTiempoRecords() {
        return arcoTiempoRecords;
    }

    public void setArcoTiempoRecords(Set<ArcoTiempo> arcoTiempoRecords) {
        this.arcoTiempoRecords = arcoTiempoRecords;
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
        return dt1.format(fechaProgramacion);
    }

    public void setFechaProgramacionFormato(String fechaProgramacionFormato) {
        this.fechaProgramacionFormato = fechaProgramacionFormato;
    }

    public String getFechaVigenciaFormato() {
        SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd/yyyy");
        return dt1.format(fechaVigencia);
    }

    public void setFechaVigenciaFormato(String fechaVigenciaFormato) {
        this.fechaVigenciaFormato = fechaVigenciaFormato;
    }

    public Set<TablaMaestra> getTablaMaestraRecords() {
        return tablaMaestraRecords;
    }

    public void setTablaMaestraRecords(Set<TablaMaestra> tablaMaestraRecords) {
        this.tablaMaestraRecords = tablaMaestraRecords;
    }
}
