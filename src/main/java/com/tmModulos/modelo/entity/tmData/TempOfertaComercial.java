package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="temp_oferta_comercial")
public class TempOfertaComercial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TempOferta")
    @SequenceGenerator(name="TempOferta", sequenceName = "temp_oferta_comercial_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "trayecto")
    private String trayecto;

    @Column(name = "tipo_dia")
    private String tipoDia;

    @Column(name = "sentido")
    private String sentido;

    @Column(name = "inicio_intervalo")
    private String inicioIntervalo;

    @Column(name = "fin_intervalo")
    private String finIntervalor;

    @Column(name = "frecuencia_objetiva")
    private String frecuenciaObjetiva;

    @Column(name = "frecuencia_minima")
    private String frecuenciaMinima;

    @Column(name = "frecuencia_maxima")
    private String frecuenciaMaxima;

    @Column(name = "capacidad_maxima")
    private String capacidadMaxima;

    @Column(name = "tipo_viaje")
    private String tipoViaje;

    @Column(name = "nodo")
    private String nodo;

    @Column(name = "refuerzo")
    private String refuerzo;

    @Column(name = "inst_inicio")
    private Time instInicio;

    @Column(name = "inst_fin")
    private Time instFin;

    @Column(name = "hora")
    private Integer hora;

    @Column(name = "minutos")
    private Integer minutos;

    @Column(name = "segundos")
    private Integer segundos;


    public TempOfertaComercial() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(String trayecto) {
        this.trayecto = trayecto;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getSentido() {
        return sentido;
    }

    public void setSentido(String sentido) {
        this.sentido = sentido;
    }

    public String getInicioIntervalo() {
        return inicioIntervalo;
    }

    public void setInicioIntervalo(String inicioIntervalo) {
        this.inicioIntervalo = inicioIntervalo;
    }

    public String getFinIntervalor() {
        return finIntervalor;
    }

    public void setFinIntervalor(String finIntervalor) {
        this.finIntervalor = finIntervalor;
    }

    public String getFrecuenciaObjetiva() {
        return frecuenciaObjetiva;
    }

    public void setFrecuenciaObjetiva(String frecuenciaObjetiva) {
        this.frecuenciaObjetiva = frecuenciaObjetiva;
    }

    public String getFrecuenciaMinima() {
        return frecuenciaMinima;
    }

    public void setFrecuenciaMinima(String frecuenciaMinima) {
        this.frecuenciaMinima = frecuenciaMinima;
    }

    public String getFrecuenciaMaxima() {
        return frecuenciaMaxima;
    }

    public void setFrecuenciaMaxima(String frecuenciaMaxima) {
        this.frecuenciaMaxima = frecuenciaMaxima;
    }

    public String getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(String capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getTipoViaje() {
        return tipoViaje;
    }

    public void setTipoViaje(String tipoViaje) {
        this.tipoViaje = tipoViaje;
    }

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public String getRefuerzo() {
        return refuerzo;
    }

    public void setRefuerzo(String refuerzo) {
        this.refuerzo = refuerzo;
    }

    public Time getInstInicio() {
        return instInicio;
    }

    public void setInstInicio(Time instInicio) {
        this.instInicio = instInicio;
    }

    public Time getInstFin() {
        return instFin;
    }

    public void setInstFin(Time instFin) {
        this.instFin = instFin;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getMinutos() {
        return minutos;
    }

    public void setMinutos(Integer minutos) {
        this.minutos = minutos;
    }

    public Integer getSegundos() {
        return segundos;
    }

    public void setSegundos(Integer segundos) {
        this.segundos = segundos;
    }
}
