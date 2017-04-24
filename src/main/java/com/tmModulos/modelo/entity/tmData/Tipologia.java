package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tipologia")
public class Tipologia {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TipologiaGenerator")
    @SequenceGenerator(name="TipologiaGenerator", sequenceName = "tipologia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo")
    private int codigo;

    @Column(name = "capacidad_picos")
    private int capacidadPicos;

    @Column(name = "capacidad_valle")
    private int capacidadValle;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipologia", cascade = CascadeType.PERSIST)
    private Set<Servicio> tipologiaRecords= new HashSet<Servicio>(0);

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

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getCapacidadPicos() {
        return capacidadPicos;
    }

    public void setCapacidadPicos(int capacidadPicos) {
        this.capacidadPicos = capacidadPicos;
    }

    public int getCapacidadValle() {
        return capacidadValle;
    }

    public void setCapacidadValle(int capacidadValle) {
        this.capacidadValle = capacidadValle;
    }

    public Set<Servicio> getTipologiaRecords() {
        return tipologiaRecords;
    }

    public void setTipologiaRecords(Set<Servicio> tipologiaRecords) {
        this.tipologiaRecords = tipologiaRecords;
    }

    public String toString(){
        return nombre;
    }
}
