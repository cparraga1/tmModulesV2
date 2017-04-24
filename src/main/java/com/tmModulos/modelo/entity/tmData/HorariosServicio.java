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

    @Column(name = "hora_inicio_a")
    private String horaInicioA;

    @Column(name = "hora_inicio_b")
    private String horaInicioB;

    @Column(name = "hora_inicio_c")
    private String horaInicioC;

    @Column(name = "hora_inicio_d")
    private String horaInicioD;

    @Column(name = "hora_fin_a")
    private String horaFinA;

    @Column(name = "hora_fin_b")
    private String horaFinB;

    @Column(name = "hora_fin_c")
    private String horaFinC;

    @Column(name = "hora_fin_d")
    private String horaFinD;

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

    public String getHoraInicioA() {
        return horaInicioA;
    }

    public void setHoraInicioA(String horaInicioA) {
        this.horaInicioA = horaInicioA;
    }

    public String getHoraInicioB() {
        return horaInicioB;
    }

    public void setHoraInicioB(String horaInicioB) {
        this.horaInicioB = horaInicioB;
    }

    public String getHoraInicioC() {
        return horaInicioC;
    }

    public void setHoraInicioC(String horaInicioC) {
        this.horaInicioC = horaInicioC;
    }

    public String getHoraInicioD() {
        return horaInicioD;
    }

    public void setHoraInicioD(String horaInicioD) {
        this.horaInicioD = horaInicioD;
    }

    public String getHoraFinA() {
        return horaFinA;
    }

    public void setHoraFinA(String horaFinA) {
        this.horaFinA = horaFinA;
    }

    public String getHoraFinB() {
        return horaFinB;
    }

    public void setHoraFinB(String horaFinB) {
        this.horaFinB = horaFinB;
    }

    public String getHoraFinC() {
        return horaFinC;
    }

    public void setHoraFinC(String horaFinC) {
        this.horaFinC = horaFinC;
    }

    public String getHoraFinD() {
        return horaFinD;
    }

    public void setHoraFinD(String horaFinD) {
        this.horaFinD = horaFinD;
    }

    public TablaMaestraServicios getTablaMaestraServicios() {
        return tablaMaestraServicios;
    }

    public void setTablaMaestraServicios(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }
}
