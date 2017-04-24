package com.tmModulos.modelo.entity.saeBogota;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="NodosSeccion",schema="dbo")
public class NodosSeccion implements Serializable{


    @Id
    @Column(name = "Macro")
    private int macro;
    @Id
    @Column(name = "Linea")
    private int linea;
    @Id
    @Column(name = "Seccion")
    private int seccion;
    @Column(name = "Tipo")
    private int tipo;
    @Id
    @Column(name = "Nodo")
    private int nodo;
    @Column(name = "ConfigLinea")
    private int configLinea;
    @Column(name = "ConfigSeccion")
    private int configSeccion;
    @Column(name = "Distancia")
    private int distancia;
    @Column(name = "Atributo")
    private int atributo;


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

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getNodo() {
        return nodo;
    }

    public void setNodo(int nodo) {
        this.nodo = nodo;
    }

    public int getConfigLinea() {
        return configLinea;
    }

    public void setConfigLinea(int configLinea) {
        this.configLinea = configLinea;
    }

    public int getConfigSeccion() {
        return configSeccion;
    }

    public void setConfigSeccion(int configSeccion) {
        this.configSeccion = configSeccion;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getAtributo() {
        return atributo;
    }

    public void setAtributo(int atributo) {
        this.atributo = atributo;
    }
}
