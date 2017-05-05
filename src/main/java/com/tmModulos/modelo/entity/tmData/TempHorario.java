package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="temp_horario")
public class TempHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="tempHorario")
    @SequenceGenerator(name="tempHorario", sequenceName = "temp_horario_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "sublinea")
    private int sublinea;

    @Column(name = "linea")
    private int linea;

    @Column(name = "instante")
    private String instante;

    @Column(name = "ruta")
    private int ruta;

    @Column(name = "punto")
    private int punto;


    public void TempHorario(){

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

    public String getInstante() {
        return instante;
    }

    public void setInstante(String instante) {
        this.instante = instante;
    }

    public int getRuta() {
        return ruta;
    }

    public void setRuta(int ruta) {
        this.ruta = ruta;
    }

    public int getPunto() {
        return punto;
    }

    public void setPunto(int punto) {
        this.punto = punto;
    }
}
