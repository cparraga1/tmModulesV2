package com.tmModulos.modelo.entity.saeBogota;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="Lineas",schema="dbo")
public class Lineas implements Serializable {

    @Id
    @Column(name = "Macro")
    private int macro;
    @Id
    @Column(name = "Linea")
    private int linea;
    @Column(name = "Label")
    private String label;
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Config")
    private int config;
    @Column(name = "Version")
    private int version;
    @Column(name = "VerGlob")
    private int verGlob;
    @Column(name = "Atributos")
    private int atributos;
    @Column(name = "IDFonia")
    private int idFonia;
    @Column(name = "Operador",nullable = true)
    private String operador;
    @Column(name = "OrdenISAE")
    private int ordenIsae;
    @Column(name = "LabelOp",nullable = true)
    private String labelOp;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVerGlob() {
        return verGlob;
    }

    public void setVerGlob(int verGlob) {
        this.verGlob = verGlob;
    }

    public int getAtributos() {
        return atributos;
    }

    public void setAtributos(int atributos) {
        this.atributos = atributos;
    }

    public int getIdFonia() {
        return idFonia;
    }

    public void setIdFonia(int idFonia) {
        this.idFonia = idFonia;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public int getOrdenIsae() {
        return ordenIsae;
    }

    public void setOrdenIsae(int ordenIsae) {
        this.ordenIsae = ordenIsae;
    }

    public String getLabelOp() {
        return labelOp;
    }

    public void setLabelOp(String labelOp) {
        this.labelOp = labelOp;
    }
}
