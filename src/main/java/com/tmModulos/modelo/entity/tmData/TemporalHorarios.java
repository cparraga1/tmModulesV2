package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="temporal_horarios")
public class TemporalHorarios {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TemporalGenerator")
    @SequenceGenerator(name="TemporalGenerator", sequenceName = "temporal_horarios_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "cuadro")
    private String cuadro;

    @Column(name = "instante")
    private Time instante;

    @Column(name = "servbus")
    private String servBus;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "macro")
    private int macro;

    @Column(name = "linea")
    private int linea;

    @Column(name = "seccion")
    private int seccion;

    @Column(name = "punto")
    private int punto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "intervalo_programacion", nullable = false)
    private IntervalosProgramacion intervalosProgramacion;

    public TemporalHorarios(String cuadro, Time instante, String servBus, String identificador, int macro, int linea, int seccion, int punto, IntervalosProgramacion intervalosProgramacion) {
        this.cuadro = cuadro;
        this.instante = instante;
        this.servBus = servBus;
        this.identificador = identificador;
        this.macro = macro;
        this.linea = linea;
        this.seccion = seccion;
        this.punto = punto;
        this.intervalosProgramacion = intervalosProgramacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCuadro() {
        return cuadro;
    }

    public void setCuadro(String cuadro) {
        this.cuadro = cuadro;
    }

    public Time getInstante() {
        return instante;
    }

    public void setInstante(Time instante) {
        this.instante = instante;
    }

    public String getServBus() {
        return servBus;
    }

    public void setServBus(String servBus) {
        this.servBus = servBus;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

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

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public int getPunto() {
        return punto;
    }

    public void setPunto(int punto) {
        this.punto = punto;
    }

    public IntervalosProgramacion getIntervalosProgramacion() {
        return intervalosProgramacion;
    }

    public void setIntervalosProgramacion(IntervalosProgramacion intervalosProgramacion) {
        this.intervalosProgramacion = intervalosProgramacion;
    }
}
