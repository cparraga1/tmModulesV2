package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="franja_horario")
public class FranjaHoraria {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="FranjaGenerator")
    @SequenceGenerator(name="FranjaGenerator", sequenceName = "franja_horario_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "hora_inicio")
    private String horaInicio;

    @Column(name = "hora_fin")
    private String horaFin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "franja")
    private Set<ServicioFranjaHorario> servicioFranjaRecords= new HashSet<ServicioFranjaHorario>(0);

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

    public Set<ServicioFranjaHorario> getServicioFranjaRecords() {
        return servicioFranjaRecords;
    }

    public void setServicioFranjaRecords(Set<ServicioFranjaHorario> servicioFranjaRecords) {
        this.servicioFranjaRecords = servicioFranjaRecords;
    }
}
