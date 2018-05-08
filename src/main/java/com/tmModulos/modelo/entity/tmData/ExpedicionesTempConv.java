package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="temp_expediciones_comp")
public class ExpedicionesTempConv {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TempExp")
    @SequenceGenerator(name="TempExp", sequenceName = "temp_expediciones_comp_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;

    @Column(name = "identificador")
    private String linea;

    @Column(name = "punto_inicio")
    private String puntoInicio;

    @Column(name = "inferido")
    private String inferido;

    @Column(name = "noo")
    private String noo;

    @Column(name = "frec")
    private String frec;

    @Column(name = "punto_fin")
    private String puntoFin;

    @Column(name = "km")
    private String km;



    @Column(name = "bus")
    private String bus;

    @Column(name = "evento")
    private String evento;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "dur")
    private String dur;

    @Column(name = "ser_bus")
    private String serBus;

    @Column(name = "des_dur")
    private String desDur;

    @Column(name = "des_frec")
    private String desFrec;

    public ExpedicionesTempConv() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getInferido() {
        return inferido;
    }

    public void setInferido(String inferido) {
        this.inferido = inferido;
    }

    public String getNoo() {
        return noo;
    }

    public void setNoo(String noo) {
        this.noo = noo;
    }

    public String getFrec() {
        return frec;
    }

    public void setFrec(String frec) {
        this.frec = frec;
    }

    public String getPuntoInicio() {
        return puntoInicio;
    }

    public void setPuntoInicio(String puntoInicio) {
        this.puntoInicio = puntoInicio;
    }

    public String getPuntoFin() {
        return puntoFin;
    }

    public void setPuntoFin(String puntoFin) {
        this.puntoFin = puntoFin;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }


    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDur() {
        return dur;
    }

    public void setDur(String dur) {
        this.dur = dur;
    }

    public String getSerBus() {
        return serBus;
    }

    public void setSerBus(String serBus) {
        this.serBus = serBus;
    }

    public String getDesDur() {
        return desDur;
    }

    public void setDesDur(String desDur) {
        this.desDur = desDur;
    }

    public String getDesFrec() {
        return desFrec;
    }

    public void setDesFrec(String desFrec) {
        this.desFrec = desFrec;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }
}
