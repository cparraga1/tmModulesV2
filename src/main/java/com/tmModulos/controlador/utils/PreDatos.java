package com.tmModulos.controlador.utils;

/**
 * Created by user on 14/12/2017.
 */
public class PreDatos {

    private int hora;
    private int minutos;
    private int segundos;
    private Double distancia;

    public PreDatos() {
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

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }
}
