package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="intervalos_identificador")
public class IntervalosIdentificador {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="IntervalosIdGnerator")
    @SequenceGenerator(name="IntervalosIdGnerator", sequenceName = "tiempo_intervalos_identificador_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "cuadro")
    private String cuadro;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idServicio")
    private Set<TiempoIntervalos> tiempoIntervalosRecords = new HashSet<TiempoIntervalos>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "idServicio")
    private Set<Intervalos> intervalosRecords = new HashSet<Intervalos>(0);

    public IntervalosIdentificador() {
    }

    public IntervalosIdentificador(String identificador, String cuadro) {
        this.identificador = identificador;
        this.cuadro = cuadro;
    }

    public Set<Intervalos> getIntervalosRecords() {
        return intervalosRecords;
    }

    public void setIntervalosRecords(Set<Intervalos> intervalosRecords) {
        this.intervalosRecords = intervalosRecords;
    }

    public String getCuadro() {
        return cuadro;
    }

    public void setCuadro(String cuadro) {
        this.cuadro = cuadro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Set<TiempoIntervalos> getTiempoIntervalosRecords() {
        return tiempoIntervalosRecords;
    }

    public void setTiempoIntervalosRecords(Set<TiempoIntervalos> tiempoIntervalosRecords) {
        this.tiempoIntervalosRecords = tiempoIntervalosRecords;
    }
}
