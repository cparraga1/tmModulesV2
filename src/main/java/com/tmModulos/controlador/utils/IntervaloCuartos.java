package com.tmModulos.controlador.utils;

import com.tmModulos.modelo.entity.tmData.IntervalosProgramacion;

import java.sql.Time;


public class IntervaloCuartos {

    private Time instante;
    private Long diferencia;
    private IntervalosProgramacion intervalo;
    private String franja;

    public IntervaloCuartos() {
    }

    public IntervaloCuartos(Time instante, IntervalosProgramacion intervalo, String franja) {
        this.instante = instante;
        this.intervalo = intervalo;
        this.franja = franja;
    }

    public IntervaloCuartos(Time instante) {
        this.instante = instante;
    }

    public Time getInstante() {
        return instante;
    }

    public void setInstante(Time instante) {
        this.instante = instante;
    }

    public Long getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(Long diferencia) {
        this.diferencia = diferencia;
    }

    public IntervalosProgramacion getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(IntervalosProgramacion intervalo) {
        this.intervalo = intervalo;
    }

    public String getFranja() {
        return franja;
    }

    public void setFranja(String franja) {
        this.franja = franja;
    }
}
