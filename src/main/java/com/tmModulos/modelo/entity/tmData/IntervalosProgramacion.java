package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="intervalos_programacion")
public class IntervalosProgramacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="IntervalosGenerator")
    @SequenceGenerator(name="IntervalosGenerator", sequenceName = "intervalos_programacion_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "inicio")
    private Time inicio;

    @Column(name = "fin")
    private Time fin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_franja", nullable = false)
    private TipoFranja tipoFranja;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intervalosProgramacion")
    private Set<TemporalHorarios> tablaServiciosRecords = new HashSet<TemporalHorarios>(0);


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "intervalosProgramacion")
    private Set<TiempoIntervalos> tiempoIntervalosRecords = new HashSet<TiempoIntervalos>(0);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Time getInicio() {
        return inicio;
    }

    public void setInicio(Time inicio) {
        this.inicio = inicio;
    }

    public Time getFin() {
        return fin;
    }

    public void setFin(Time fin) {
        this.fin = fin;
    }

    public Set<TemporalHorarios> getTablaServiciosRecords() {
        return tablaServiciosRecords;
    }

    public void setTablaServiciosRecords(Set<TemporalHorarios> tablaServiciosRecords) {
        this.tablaServiciosRecords = tablaServiciosRecords;
    }

    public Set<TiempoIntervalos> getTiempoIntervalosRecords() {
        return tiempoIntervalosRecords;
    }

    public void setTiempoIntervalosRecords(Set<TiempoIntervalos> tiempoIntervalosRecords) {
        this.tiempoIntervalosRecords = tiempoIntervalosRecords;
    }

    public TipoFranja getTipoFranja() {
        return tipoFranja;
    }

    public void setTipoFranja(TipoFranja tipoFranja) {
        this.tipoFranja = tipoFranja;
    }

//    @Override
//    public String toString() {
//        return convertirTiempo(inicio)+"-"+convertirTiempo(fin);
//    }


}
