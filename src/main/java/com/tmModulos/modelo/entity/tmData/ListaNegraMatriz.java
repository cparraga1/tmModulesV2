package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="lista_negra_matriz")
public class ListaNegraMatriz {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="listaNegraGnerator")
    @SequenceGenerator(name="listaNegraGnerator", sequenceName = "lista_negra_matriz_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;


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
}
