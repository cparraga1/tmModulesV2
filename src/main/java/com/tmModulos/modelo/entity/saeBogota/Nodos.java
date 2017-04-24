package com.tmModulos.modelo.entity.saeBogota;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Nodos",schema="dbo")
public class Nodos {

    @Id
    @Column(name = "Id")
    private int id;
    @Column(name = "Tipo")
    private int tipo;
    @Column(name = "Nombre")
    private String nombre;

    @Column(name = "PosX")
    private int posX;
    @Column(name = "PosY")
    private int posY;
    @Column(name = "Label")
    private String label;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
