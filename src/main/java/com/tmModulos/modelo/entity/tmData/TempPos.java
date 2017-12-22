package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="temp_pos")
public class TempPos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="tempHorariopos")
    @SequenceGenerator(name="tempHorariopos", sequenceName = "temp_pos_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "sublinea")
    private int sublinea;

    @Column(name = "linea")
    private int linea;

    @Column(name = "instante")
    private Time instante;

    @Column(name = "ruta")
    private int ruta;

    @Column(name = "punto")
    private int punto;

    @Column(name = "evento")
    private int evento;

    @Column(name = "hora")
    private int hora;

    @Column(name = "minutos")
    private int minutos;

    @Column(name = "segundos")
    private int segundos;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "inst")
    private String inst;


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

    public Time getInstante() {
        return instante;
    }

    public void setInstante(Time instante) {
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

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public String getInst() {
        return inst;
    }

    public void setInst(String inst) {
        this.inst = inst;
    }
}
