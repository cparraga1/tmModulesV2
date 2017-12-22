package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.math.BigInteger;
import java.sql.Time;

@Entity
@Table(name="temp_expediciones")
public class ExpedicionesTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TempExp")
    @SequenceGenerator(name="TempExp", sequenceName = "temp_expediciones_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;

    @Column(name = "inst_inicio")
    private Time instInicio;

    @Column(name = "inst_fin")
    private Time instFin;

    @Column(name = "punto_inicio")
    private Integer puntoInicio;

    @Column(name = "dia")
    private Integer dia;

    @Column(name = "punto_fin")
    private Integer puntoFin;

    @Column(name = "km")
    private String km;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "id_c")
    private String idC;

    @Column(name = "hora")
    private Integer hora;

    @Column(name = "minutos")
    private Integer minutos;

    @Column(name = "segundos")
    private Integer segundos;


    public ExpedicionesTemporal() {
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getPuntoInicio() {
        return puntoInicio;
    }

    public void setPuntoInicio(Integer puntoInicio) {
        this.puntoInicio = puntoInicio;
    }

    public Integer getPuntoFin() {
        return puntoFin;
    }

    public void setPuntoFin(Integer puntoFin) {
        this.puntoFin = puntoFin;
    }


    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
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

    public String getIdC() {
        return idC;
    }

    public void setIdC(String idC) {
        this.idC = idC;
    }
}
