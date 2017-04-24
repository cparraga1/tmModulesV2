package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="tiempo_intervalos")
public class TiempoIntervalos {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TiempoGnerator")
    @SequenceGenerator(name="TiempoGnerator", sequenceName = "tiempo_intervalos_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "valor")
    private Time valor;


    @Column(name = "instante")
    private Integer instante;



    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "servicio", nullable = false)
    private ServicioTipoDia idServicio;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tiempo_programacion", nullable = false)
    private IntervalosProgramacion intervalosProgramacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gis_intervalos", nullable = false)
    private GisIntervalos gisIntervalos;

    public TiempoIntervalos() {
    }

    public TiempoIntervalos(Time valor, ServicioTipoDia idServicio, IntervalosProgramacion intervalosProgramacion,int instante,GisIntervalos gisIntervalos) {
        this.valor = valor;
        this.idServicio = idServicio;
        this.intervalosProgramacion = intervalosProgramacion;
        this.instante = instante;
        this.gisIntervalos = gisIntervalos;
    }

    public ServicioTipoDia getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(ServicioTipoDia idServicio) {
        this.idServicio = idServicio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Time getValor() {
        return valor;
    }

    public void setValor(Time valor) {
        this.valor = valor;
    }



    public IntervalosProgramacion getIntervalosProgramacion() {
        return intervalosProgramacion;
    }

    public void setIntervalosProgramacion(IntervalosProgramacion intervalosProgramacion) {
        this.intervalosProgramacion = intervalosProgramacion;
    }

    public Integer getInstante() {
        return instante;
    }

    public void setInstante(Integer instante) {
        this.instante = instante;
    }

    public GisIntervalos getGisIntervalos() {
        return gisIntervalos;
    }

    public void setGisIntervalos(GisIntervalos gisIntervalos) {
        this.gisIntervalos = gisIntervalos;
    }
}
