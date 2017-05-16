package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name="hora_franja")
public class HoraFranja {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="HoraGnerator")
    @SequenceGenerator(name="HoraGnerator", sequenceName = "hora_franja_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "hora_inicio")
    private Time horaInicio;

    @Column(name = "hora_fin")
    private Time horaFin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_franja", nullable = false)
    private TipoFranja tipoFranja;

    public HoraFranja() {
    }

    public Time getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Time horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Time getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Time horaFin) {
        this.horaFin = horaFin;
    }

    public TipoFranja getTipoFranja() {
        return tipoFranja;
    }

    public void setTipoFranja(TipoFranja tipoFranja) {
        this.tipoFranja = tipoFranja;
    }
}
