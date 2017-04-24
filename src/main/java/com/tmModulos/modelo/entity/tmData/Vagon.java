package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="vagon")
public class Vagon {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="VagonGenerator")
    @SequenceGenerator(name="VagonGenerator", sequenceName = "vagon_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numeracion")
    private Integer numeracion;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "estacion", nullable = false)
    private Estacion estacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vagon",cascade = CascadeType.ALL)
    private Set<Nodo> estacionRecords= new HashSet<Nodo>(0);


    public Vagon() {
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

    public Integer getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(Integer numeracion) {
        this.numeracion = numeracion;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public void setEstacion(Estacion estacion) {
        this.estacion = estacion;
    }

    public Set<Nodo> getEstacionRecords() {
        return estacionRecords;
    }

    public void setEstacionRecords(Set<Nodo> estacionRecords) {
        this.estacionRecords = estacionRecords;
    }
}
