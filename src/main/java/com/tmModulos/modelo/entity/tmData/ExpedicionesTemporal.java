package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.math.BigInteger;

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

    @Column(name = "punto_inicio")
    private Integer puntoInicio;

    @Column(name = "punto_fin")
    private Integer puntoFin;

    @Column(name = "km")
    private String km;

    @Column(name = "identificador")
    private String identificador;

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
}
