package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="velocidad_programada")
public class VelocidadProgramada {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="VprogGenerator")
    @SequenceGenerator(name="VprogGenerator", sequenceName = "velocidad_programada_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "optima_am")
    private Double optimoAM;

    @Column(name = "optima_valle")
    private Double optimoValle;

    @Column(name = "optima_pm")
    private Double optimoPM;

    @Column(name = "minima_am")
    private Double minimoAM;

    @Column(name = "minima_valle")
    private Double minimoValle;

    @Column(name = "minima_pm")
    private Double minimoPM;

    @Column(name = "maxima_am")
    private Double maximoAM;

    @Column(name = "maxima_valle")
    private Double maximoValle;

    @Column(name = "maxima_pm")
    private Double maximoPM;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "velocidadProgramada", cascade = CascadeType.REMOVE)
    private TablaMaestraServicios tablaMaestraServicios;


    public VelocidadProgramada() {
    }

    public VelocidadProgramada(Double optimoAM, Double optimoValle, Double optimoPM, Double minimoAM, Double minimoValle, Double minimoPM, Double maximoAM, Double maximoValle, Double maximoPM) {
        this.optimoAM = optimoAM;
        this.optimoValle = optimoValle;
        this.optimoPM = optimoPM;
        this.minimoAM = minimoAM;
        this.minimoValle = minimoValle;
        this.minimoPM = minimoPM;
        this.maximoAM = maximoAM;
        this.maximoValle = maximoValle;
        this.maximoPM = maximoPM;
    }

    public Double getOptimoAM() {
        return optimoAM;
    }

    public void setOptimoAM(Double optimoAM) {
        this.optimoAM = optimoAM;
    }

    public Double getOptimoValle() {
        return optimoValle;
    }

    public void setOptimoValle(Double optimoValle) {
        this.optimoValle = optimoValle;
    }

    public Double getOptimoPM() {
        return optimoPM;
    }

    public void setOptimoPM(Double optimoPM) {
        this.optimoPM = optimoPM;
    }

    public Double getMinimoAM() {
        return minimoAM;
    }

    public void setMinimoAM(Double minimoAM) {
        this.minimoAM = minimoAM;
    }

    public Double getMinimoValle() {
        return minimoValle;
    }

    public void setMinimoValle(Double minimoValle) {
        this.minimoValle = minimoValle;
    }

    public Double getMinimoPM() {
        return minimoPM;
    }

    public void setMinimoPM(Double minimoPM) {
        this.minimoPM = minimoPM;
    }

    public Double getMaximoAM() {
        return maximoAM;
    }

    public void setMaximoAM(Double maximoAM) {
        this.maximoAM = maximoAM;
    }

    public Double getMaximoValle() {
        return maximoValle;
    }

    public void setMaximoValle(Double maximoValle) {
        this.maximoValle = maximoValle;
    }

    public Double getMaximoPM() {
        return maximoPM;
    }

    public void setMaximoPM(Double maximoPM) {
        this.maximoPM = maximoPM;
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
}
