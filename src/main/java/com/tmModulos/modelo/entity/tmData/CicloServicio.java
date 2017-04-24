package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ciclo_servicio")
public class CicloServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="CicloGenerator")
    @SequenceGenerator(name="CicloGenerator", sequenceName = "ciclo_servicio_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;


    @Column(name = "optimo_inicio")
    private String optimoInicio;

    @Column(name = "minimo_inicio")
    private String minimoInicio;

    @Column(name = "maximo_inicio")
    private String maximoInicio;

    @Column(name = "optimo_am")
    private String optimoAM;

    @Column(name = "minimo_am")
    private String minimoAM;

    @Column(name = "maximo_am")
    private String maximoAM;

    @Column(name = "optimo_valle")
    private String optimoValle;

    @Column(name = "minimo_valle")
    private String minimoValle;

    @Column(name = "maximo_valle")
    private String maximoValle;

    @Column(name = "optimo_pm")
    private String optimoPM;

    @Column(name = "minimo_pm")
    private String minimoPM;

    @Column(name = "maximo_pm")
    private String maximoPM;

    @Column(name = "optimo_cierre")
    private String optimoCierre;

    @Column(name = "minimo_cierre")
    private String minimoCierre;

    @Column(name = "maximo_cierre")
    private String maximoCierre;



    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cicloServicio", cascade = CascadeType.REMOVE)
    private TablaMaestraServicios tablaMaestraServicios;

    public CicloServicio(String optimoInicio, String optimoAM, String optimoValle, String optimoPM, String optimoCierre) {
        this.optimoInicio = optimoInicio;
        this.optimoAM = optimoAM;
        this.optimoValle = optimoValle;
        this.optimoPM = optimoPM;
        this.optimoCierre = optimoCierre;
    }

    public CicloServicio(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }

    public CicloServicio() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOptimoInicio() {
        return optimoInicio;
    }

    public void setOptimoInicio(String optimoInicio) {
        this.optimoInicio = optimoInicio;
    }

    public String getMinimoInicio() {
        return minimoInicio;
    }

    public void setMinimoInicio(String minimoInicio) {
        this.minimoInicio = minimoInicio;
    }

    public String getMaximoInicio() {
        return maximoInicio;
    }

    public void setMaximoInicio(String maximoInicio) {
        this.maximoInicio = maximoInicio;
    }

    public String getOptimoAM() {
        return optimoAM;
    }

    public void setOptimoAM(String optimoAM) {
        this.optimoAM = optimoAM;
    }

    public String getMinimoAM() {
        return minimoAM;
    }

    public void setMinimoAM(String minimoAM) {
        this.minimoAM = minimoAM;
    }

    public String getMaximoAM() {
        return maximoAM;
    }

    public void setMaximoAM(String maximoAM) {
        this.maximoAM = maximoAM;
    }

    public String getOptimoValle() {
        return optimoValle;
    }

    public void setOptimoValle(String optimoValle) {
        this.optimoValle = optimoValle;
    }

    public String getMinimoValle() {
        return minimoValle;
    }

    public void setMinimoValle(String minimoValle) {
        this.minimoValle = minimoValle;
    }

    public String getMaximoValle() {
        return maximoValle;
    }

    public void setMaximoValle(String maximoValle) {
        this.maximoValle = maximoValle;
    }

    public String getOptimoPM() {
        return optimoPM;
    }

    public void setOptimoPM(String optimoPM) {
        this.optimoPM = optimoPM;
    }

    public String getMinimoPM() {
        return minimoPM;
    }

    public void setMinimoPM(String minimoPM) {
        this.minimoPM = minimoPM;
    }

    public String getMaximoPM() {
        return maximoPM;
    }

    public void setMaximoPM(String maximoPM) {
        this.maximoPM = maximoPM;
    }

    public String getOptimoCierre() {
        return optimoCierre;
    }

    public void setOptimoCierre(String optimoCierre) {
        this.optimoCierre = optimoCierre;
    }

    public String getMinimoCierre() {
        return minimoCierre;
    }

    public void setMinimoCierre(String minimoCierre) {
        this.minimoCierre = minimoCierre;
    }

    public String getMaximoCierre() {
        return maximoCierre;
    }

    public void setMaximoCierre(String maximoCierre) {
        this.maximoCierre = maximoCierre;
    }

    public TablaMaestraServicios getTablaMaestraServicios() {
        return tablaMaestraServicios;
    }

    public void setTablaMaestraServicios(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }
}
