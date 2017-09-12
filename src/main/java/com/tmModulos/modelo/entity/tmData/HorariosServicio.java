package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="horarios_servicio")
public class HorariosServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="HorariosGenerator")
    @SequenceGenerator(name="HorariosGenerator", sequenceName = "horarios_servicio_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "hora_inicio_c")
    private String horaInicioUsuarioA;

    @Column(name = "hora_inicio_d")
    private String horaInicioUsuarioB;

    @Column(name = "hora_inicio_a")
    private String horaInicioProgA;

    @Column(name = "hora_inicio_b")
    private String horaInicioProgB;

    @Column(name = "hora_fin_c")
    private String horaFinUsuarioA;

    @Column(name = "hora_fin_d")
    private String horaFinUsuarioB;

    @Column(name = "hora_fin_a")
    private String horaFinProgA;

    @Column(name = "hora_fin_b")
    private String horaFinProgB;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "horariosServicio", cascade = CascadeType.REMOVE)
    private TablaMaestraServicios tablaMaestraServicios;


    public HorariosServicio() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public TablaMaestraServicios getTablaMaestraServicios() {
        return tablaMaestraServicios;
    }

    public void setTablaMaestraServicios(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }

    public String getHoraInicioUsuarioA() {
        return horaInicioUsuarioA;
    }

    public void setHoraInicioUsuarioA(String horaInicioUsuarioA) {
        this.horaInicioUsuarioA = horaInicioUsuarioA;
    }

    public String getHoraInicioUsuarioB() {
        return horaInicioUsuarioB;
    }

    public void setHoraInicioUsuarioB(String horaInicioUsuarioB) {
        this.horaInicioUsuarioB = horaInicioUsuarioB;
    }

    public String getHoraInicioProgA() {
        return horaInicioProgA;
    }

    public void setHoraInicioProgA(String horaInicioProgA) {
        this.horaInicioProgA = horaInicioProgA;
    }

    public String getHoraInicioProgB() {
        return horaInicioProgB;
    }

    public void setHoraInicioProgB(String horaInicioProgB) {
        this.horaInicioProgB = horaInicioProgB;
    }

    public String getHoraFinUsuarioA() {
        return horaFinUsuarioA;
    }

    public void setHoraFinUsuarioA(String horaFinUsuarioA) {
        this.horaFinUsuarioA = horaFinUsuarioA;
    }

    public String getHoraFinUsuarioB() {
        return horaFinUsuarioB;
    }

    public void setHoraFinUsuarioB(String horaFinUsuarioB) {
        this.horaFinUsuarioB = horaFinUsuarioB;
    }

    public String getHoraFinProgA() {
        return horaFinProgA;
    }

    public void setHoraFinProgA(String horaFinProgA) {
        this.horaFinProgA = horaFinProgA;
    }

    public String getHoraFinProgB() {
        return horaFinProgB;
    }

    public void setHoraFinProgB(String horaFinProgB) {
        this.horaFinProgB = horaFinProgB;
    }
}
