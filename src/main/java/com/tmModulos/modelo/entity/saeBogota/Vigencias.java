package com.tmModulos.modelo.entity.saeBogota;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="Vigencias",schema="dbo")
public class Vigencias implements Serializable {

    @Id
    @Column(name = "Fecha")
    private Timestamp fecha;
    @Id
    @Column(name = "Macro")
    private int macro;
    @Id
    @Column(name = "Linea")
    private int linea;
    @Column(name = "TipoDia")
    private String tipoDia;
    @Column(name = "Config",nullable = true)
    private Integer config;


    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

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

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(Integer config) {
        this.config = config;
    }
}
