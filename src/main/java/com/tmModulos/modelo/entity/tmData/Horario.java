package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="horario")
public class Horario {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="Horarios2Generator")
    @SequenceGenerator(name="Horarios2Generator", sequenceName = "Horario_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;

    @Column(name = "tipo_horario")
    private String tipoHorario;

    @Column(name = "config")
    private int config;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio", nullable = false)
    private Servicio servicio;



    public Horario() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getTipoHorario() {
        return tipoHorario;
    }

    public void setTipoHorario(String tipoHorario) {
        this.tipoHorario = tipoHorario;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

}
