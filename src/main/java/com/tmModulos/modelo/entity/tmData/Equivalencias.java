package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="equivalencias_migracion")
public class Equivalencias {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="EqGenerator")
    @SequenceGenerator(name="EqGenerator", sequenceName = "equivalencias_migracion_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "sublinea")
    private int sublinea;

    @Column(name = "linea")
    private int linea;

    @Column(name = "sentido")
    private int sentido;

    @Column(name = "ruta")
    private int ruta;

    @Column(name = "punto_inicio")
    private int puntoInicio;

    @Column(name = "punto_fin")
    private int puntoFin;


    public Equivalencias() {
    }

    public int getSublinea() {
        return sublinea;
    }

    public void setSublinea(int sublinea) {
        this.sublinea = sublinea;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getSentido() {
        return sentido;
    }

    public void setSentido(int sentido) {
        this.sentido = sentido;
    }

    public int getRuta() {
        return ruta;
    }

    public void setRuta(int ruta) {
        this.ruta = ruta;
    }

    public int getPuntoInicio() {
        return puntoInicio;
    }

    public void setPuntoInicio(int puntoInicio) {
        this.puntoInicio = puntoInicio;
    }

    public int getPuntoFin() {
        return puntoFin;
    }

    public void setPuntoFin(int puntoFin) {
        this.puntoFin = puntoFin;
    }
}
