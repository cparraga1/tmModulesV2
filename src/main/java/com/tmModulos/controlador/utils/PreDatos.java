package com.tmModulos.controlador.utils;

import java.util.Comparator;

/**
 * Created by user on 14/12/2017.
 */
public class PreDatos{

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

    @Override
    public String toString() {
        String horaValor = ""+hora;
        String minutoValor = ""+minutos;
        String segundosValor = ""+segundos;
        if(String.valueOf(minutos).length() == 1){
            minutoValor = "0"+minutos;
        }
        if(String.valueOf(hora).length() == 1){
            horaValor = "0"+hora;
        }
        if(String.valueOf(segundos).length() == 1){
            segundosValor = "0"+segundos;
        }
        return horaValor+":"+minutoValor+":"+segundosValor;
    }
}
