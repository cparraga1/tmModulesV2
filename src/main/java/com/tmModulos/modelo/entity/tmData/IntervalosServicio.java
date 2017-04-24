package com.tmModulos.modelo.entity.tmData;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name="intervalos_servicio")
public class IntervalosServicio {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TablaSGenerator")
    @SequenceGenerator(name="TablaSGenerator", sequenceName = "tabla_maestra_servicios_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "promedio")
    private Integer promedio;

    @Column(name = "minimo")
    private Integer minimo;

    @Column(name = "maximo")
    private Integer maximo;

    @Column(name = "moda")
    private Integer moda;

    @Column(name = "buses_hora_promedio")
    private int busesPromedio;

    @Column(name = "buses_hora_minimo")
    private int busesMinimo;

    @Column(name = "buses_hora_maximo")
    private int busesMaximo;

    @Column(name = "buses_hora_moda")
    private int busesModa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_franja", nullable = false)
    private TipoFranja tipoFranja;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tabla_servicios", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TablaMaestraServicios tablaMaestraServicios;

    public IntervalosServicio() {
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getPromedio() {
        return promedio;
    }

    public void setPromedio(Integer promedio) {
        this.promedio = promedio;
    }

    public Integer getMinimo() {
        return minimo;
    }

    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Integer getModa() {
        return moda;
    }

    public void setModa(Integer moda) {
        this.moda = moda;
    }

    public int getBusesPromedio() {
        return busesPromedio;
    }

    public void setBusesPromedio(int busesPromedio) {
        this.busesPromedio = busesPromedio;
    }

    public int getBusesMinimo() {
        return busesMinimo;
    }

    public void setBusesMinimo(int busesMinimo) {
        this.busesMinimo = busesMinimo;
    }

    public int getBusesMaximo() {
        return busesMaximo;
    }

    public void setBusesMaximo(int busesMaximo) {
        this.busesMaximo = busesMaximo;
    }

    public int getBusesModa() {
        return busesModa;
    }

    public void setBusesModa(int busesModa) {
        this.busesModa = busesModa;
    }

    public TipoFranja getTipoFranja() {
        return tipoFranja;
    }

    public void setTipoFranja(TipoFranja tipoFranja) {
        this.tipoFranja = tipoFranja;
    }

    public TablaMaestraServicios getTablaMaestraServicios() {
        return tablaMaestraServicios;
    }

    public void setTablaMaestraServicios(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }
}
