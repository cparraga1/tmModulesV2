package com.tmModulos.modelo.entity.saeBogota;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="Secciones",schema="dbo")
public class Secciones implements Serializable {


    @Id
    @Column(name = "Macro")
    private int macro;
    @Id
    @Column(name = "Linea")
    private int linea;
    @Id
    @Column(name = "Seccion")
    private int seccion;
    @Column(name = "Label")
    private String label;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Config")
    private int config;
    @Column(name = "ConfigPuntos")
    private int configPuntos;
    @Column(name = "Version")
    private int version;
    @Column(name = "VerPuntos")
    private int verPuntos;
    @Column(name = "VerParadas")
    private int verParadas;
    @Column(name = "VerDisplays")
    private int verDisplays;
    @Column(name = "VerTramos")
    private int verTramos;


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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public int getConfigPuntos() {
        return configPuntos;
    }

    public void setConfigPuntos(int configPuntos) {
        this.configPuntos = configPuntos;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVerPuntos() {
        return verPuntos;
    }

    public void setVerPuntos(int verPuntos) {
        this.verPuntos = verPuntos;
    }

    public int getVerParadas() {
        return verParadas;
    }

    public void setVerParadas(int verParadas) {
        this.verParadas = verParadas;
    }

    public int getVerDisplays() {
        return verDisplays;
    }

    public void setVerDisplays(int verDisplays) {
        this.verDisplays = verDisplays;
    }

    public int getVerTramos() {
        return verTramos;
    }

    public void setVerTramos(int verTramos) {
        this.verTramos = verTramos;
    }
}
