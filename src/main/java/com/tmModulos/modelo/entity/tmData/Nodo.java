package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="nodo")
public class Nodo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="NodoGenerator")
    @SequenceGenerator(name="NodoGenerator", sequenceName = "nodo_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo")
    private Integer codigo;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vagon", nullable = false)
    private Vagon vagon;


    public Nodo() {
    }

    public Nodo(String nombre) {
        this.nombre = nombre;
    }

    public Nodo(String nombre, Integer codigo) {
        this.nombre = nombre;
        this.codigo = codigo;
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

    public Integer getCodigo() {
        return codigo;
    }


    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Vagon getVagon() {
        return vagon;
    }

    public void setVagon(Vagon vagon) {
        this.vagon = vagon;
    }
}
