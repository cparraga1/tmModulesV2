package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="gis_servicios")
public class GisServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="GServiciosGenerator")
    @SequenceGenerator(name="GServiciosGenerator", sequenceName = "gis_servicios_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "trayecto")
    private Integer trayecto;

    @Column(name = "linea")
    private int linea;

    @Column(name = "sentido")
    private int sentido;

    @Column(name = "nodo_inicial")
    private String nodoIncial;

    @Column(name = "nodo_final")
    private String nodoFinal;

    @Column(name = "identificador")
    private String identificador;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicio")
    private Set<ArcoTiempo> arcoTiempoRecords = new HashSet<ArcoTiempo>(0);

    public GisServicio() {
    }

    public GisServicio(Integer trayecto, int linea, String nodoIncial, String nodoFinal) {
        this.trayecto = trayecto;
        this.linea = linea;
        this.nodoIncial = nodoIncial;
        this.nodoFinal = nodoFinal;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(Integer trayecto) {
        this.trayecto = trayecto;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public String getNodoIncial() {
        return nodoIncial;
    }

    public void setNodoIncial(String nodoIncial) {
        this.nodoIncial = nodoIncial;
    }

    public String getNodoFinal() {
        return nodoFinal;
    }

    public void setNodoFinal(String nodoFinal) {
        this.nodoFinal = nodoFinal;
    }

    public Set<ArcoTiempo> getArcoTiempoRecords() {
        return arcoTiempoRecords;
    }

    public void setArcoTiempoRecords(Set<ArcoTiempo> arcoTiempoRecords) {
        this.arcoTiempoRecords = arcoTiempoRecords;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public int getSentido() {
        return sentido;
    }

    public void setSentido(int sentido) {
        this.sentido = sentido;
    }
}
