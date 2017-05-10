package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="servicio_distancia")
public class ServicioDistancia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TrayectoGenerator")
    @SequenceGenerator(name="TrayectoGenerator", sequenceName = "trayecto_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;
    @Column(name = "ruta")
    private String ruta;
    @Column(name = "macro")
    private int macro;
    @Column(name = "linea")
    private int linea;
    @Column(name = "seccion")
    private int seccion;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "nombre_linea")
    private String nombreLinea;

    @Column(name = "sentido")
    private String sentido;

    @Column(name = "config")
    private Integer config;

    @Column(name = "id_sentido")
    private Integer id_sentido;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicioDistancia")
    private Set<DistanciaNodos> distanciaNodosRecords = new HashSet<DistanciaNodos>(0);



    public ServicioDistancia() {
    }


    public ServicioDistancia(String ruta, int macro, int linea, int seccion) {
        this.ruta = ruta;
        this.macro = macro;
        this.linea = linea;
        this.seccion = seccion;
    }

    public ServicioDistancia(String ruta, int macro, int linea, int seccion, String nombreLinea, String sentido, int config, int id_sentido) {
        this.ruta = ruta;
        this.macro = macro;
        this.linea = linea;
        this.seccion = seccion;
        this.nombreLinea = nombreLinea;
        this.sentido = sentido;
        this.config = config;
        this.id_sentido = id_sentido;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public int getMacro() {
        return macro;
    }

    public void setMacro(int macro) {
        this.macro = macro;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public Set<DistanciaNodos> getDistanciaNodosRecords() {
        return distanciaNodosRecords;
    }

    public void setDistanciaNodosRecords(Set<DistanciaNodos> distanciaNodosRecords) {
        this.distanciaNodosRecords = distanciaNodosRecords;
    }


    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombreLinea() {
        return nombreLinea;
    }

    public void setNombreLinea(String nombreLinea) {
        this.nombreLinea = nombreLinea;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public Integer getConfig() {
        return config;
    }

    public void setConfig(Integer config) {
        this.config = config;
    }

    public Integer getId_sentido() {
        return id_sentido;
    }

    public void setId_sentido(Integer id_sentido) {
        this.id_sentido = id_sentido;
    }
}
