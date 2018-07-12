package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="aplicacion")
public class Aplicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="AplicacionGenerator")
    @SequenceGenerator(name="AplicacionGenerator", sequenceName = "aplicacion_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "codigo")
    private int codigo;

    public Aplicacion() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
}
