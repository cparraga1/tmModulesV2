package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="servicio_franja_horario")
public class ServicioFranjaHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="SerFranjaGenerator")
    @SequenceGenerator(name="SerFranjaGenerator", sequenceName = "servicio_franja_horario_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "tipo_franja")
    private int tipoFranja;

//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "servicio", nullable = false)
//    private Servicio servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "franja", nullable = false)
    private FranjaHoraria franja;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTipoFranja() {
        return tipoFranja;
    }

    public void setTipoFranja(int tipoFranja) {
        this.tipoFranja = tipoFranja;
    }

//    public Servicio getServicio() {
//        return servicio;
//    }
//
//    public void setServicio(Servicio servicio) {
//        this.servicio = servicio;
//    }

    public FranjaHoraria getFranja() {
        return franja;
    }

    public void setFranja(FranjaHoraria franja) {
        this.franja = franja;
    }
}
