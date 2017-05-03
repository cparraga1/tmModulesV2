package com.tmModulos.modelo.entity.tmData;

import javax.persistence.Column;
import java.util.Date;

/**
 * Created by nataly on 5/1/2017.
 */
public class Expediciones {


    private Date horaInicio;


    private Date horaFin;


    private Integer puntoInicio;


    private Integer puntoFin;


    private String km;


    private String identificador;


    public Expediciones() {
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getPuntoInicio() {
        return puntoInicio;
    }

    public void setPuntoInicio(Integer puntoInicio) {
        this.puntoInicio = puntoInicio;
    }

    public Integer getPuntoFin() {
        return puntoFin;
    }

    public void setPuntoFin(Integer puntoFin) {
        this.puntoFin = puntoFin;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
}
