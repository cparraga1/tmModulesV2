package com.tmModulos.controlador.utils;

public class TipoHorario {

    private String nombre;
    private String acronimo;

    public TipoHorario() {

    }

    public TipoHorario(String nombre, String acronimo) {
        this.nombre = nombre;
        this.acronimo = acronimo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAcronimo() {
        return acronimo;
    }

    public void setAcronimo(String acronimo) {
        this.acronimo = acronimo;
    }
}
