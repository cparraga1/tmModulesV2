package com.tmModulos.modelo.entity.tmData;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="intervalos")
public class Intervalos {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="InteGenerator")
    @SequenceGenerator(name="InteGenerator", sequenceName = "intervalos_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "tipo_calculo")
    private String tipoCalculo;

    @Column(name = "valor_inicio")
    private Double valorInicio;

    @Column(name = "valor_am")
    private Double valorAM;

    @Column(name = "valor_valle")
    private Double valorValle;

    @Column(name = "valor_pm")
    private Double valorPM;

    @Column(name = "valor_cierre")
    private Double valorCierre;

    @Column(name = "buses_inicio")
    private int busesInicio;

    @Column(name = "buses_am")
    private int busesAM;

    @Column(name = "buses_valle")
    private int busesValle;

    @Column(name = "buses_pm")
    private int busesPM;

    @Column(name = "buses_cierre")
    private int busesCierre;



    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "identificador", nullable = false)
    private ServicioTipoDia idServicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tabla_maestra", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TablaMaestraServicios tablaMaestraServicios;

    public Intervalos(String tipoCalculo, Double valorInicio, Double valorAM, Double valorValle, Double valorPM, Double valorCierre,ServicioTipoDia idServicio,TablaMaestraServicios tablaMaestraServicios) {
        this.tipoCalculo = tipoCalculo;
        this.valorInicio = valorInicio;
        this.valorAM = valorAM;
        this.valorValle = valorValle;
        this.valorPM = valorPM;
        this.valorCierre = valorCierre;
        this.idServicio = idServicio;
        this.tablaMaestraServicios=tablaMaestraServicios;
    }



    public Intervalos() {
    }

    public Intervalos(String tipoCalculo, Double valorInicio, Double valorAM, Double valorValle, Double valorPM, Double valorCierre, int busesInicio, int busesAM, int busesValle, int busesPM, int busesCierre, ServicioTipoDia idServicio, TablaMaestraServicios tablaMaestraServicios) {
        this.tipoCalculo = tipoCalculo;
        this.valorInicio = valorInicio;
        this.valorAM = valorAM;
        this.valorValle = valorValle;
        this.valorPM = valorPM;
        this.valorCierre = valorCierre;
        this.busesInicio = busesInicio;
        this.busesAM = busesAM;
        this.busesValle = busesValle;
        this.busesPM = busesPM;
        this.busesCierre = busesCierre;
        this.idServicio = idServicio;
        this.tablaMaestraServicios = tablaMaestraServicios;
    }

    public TablaMaestraServicios getTablaMaestraServicios() {
        return tablaMaestraServicios;
    }

    public void setTablaMaestraServicios(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }

    public ServicioTipoDia getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(ServicioTipoDia idServicio) {
        this.idServicio = idServicio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipoCalculo() {
        return tipoCalculo;
    }

    public void setTipoCalculo(String tipoCalculo) {
        this.tipoCalculo = tipoCalculo;
    }

    public Double getValorInicio() {
        return valorInicio;
    }

    public void setValorInicio(Double valorInicio) {
        this.valorInicio = valorInicio;
    }

    public Double getValorAM() {
        return valorAM;
    }

    public void setValorAM(Double valorAM) {
        this.valorAM = valorAM;
    }

    public Double getValorValle() {
        return valorValle;
    }

    public void setValorValle(Double valorValle) {
        this.valorValle = valorValle;
    }

    public Double getValorPM() {
        return valorPM;
    }

    public void setValorPM(Double valorPM) {
        this.valorPM = valorPM;
    }

    public Double getValorCierre() {
        return valorCierre;
    }

    public void setValorCierre(Double valorCierre) {
        this.valorCierre = valorCierre;
    }

    public int getBusesInicio() {
        return busesInicio;
    }

    public void setBusesInicio(int busesInicio) {
        this.busesInicio = busesInicio;
    }

    public int getBusesAM() {
        return busesAM;
    }

    public void setBusesAM(int busesAM) {
        this.busesAM = busesAM;
    }

    public int getBusesValle() {
        return busesValle;
    }

    public void setBusesValle(int busesValle) {
        this.busesValle = busesValle;
    }

    public int getBusesPM() {
        return busesPM;
    }

    public void setBusesPM(int busesPM) {
        this.busesPM = busesPM;
    }

    public int getBusesCierre() {
        return busesCierre;
    }

    public void setBusesCierre(int busesCierre) {
        this.busesCierre = busesCierre;
    }
}
