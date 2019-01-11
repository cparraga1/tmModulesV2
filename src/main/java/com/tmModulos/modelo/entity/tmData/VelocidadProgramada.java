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

    @Column(name = "optima_primera")
    private Double optimoPrimera;

    @Column(name = "minima_primera")
    private Double minimoPrimera;

    @Column(name = "maxima_primera")
    private Double maximoPrimera;

    @Column(name = "optima_segunda")
    private Double optimoSegunda;

    @Column(name = "minima_segunda")
    private Double minimoSegunda;

    @Column(name = "maxima_segunda")
    private Double maximoSegunda;

    @Column(name = "optima_tercera")
    private Double optimoTercera;

    @Column(name = "minima_tercera")
    private Double minimoTercera;

    @Column(name = "maxima_tercera")
    private Double maximoTercera;

    @Column(name = "optima_cuarta")
    private Double optimoCuarta;

    @Column(name = "minima_cuarta")
    private Double minimoCuarta;

    @Column(name = "maxima_cuarta")
    private Double maximoCuarta;

    @Column(name = "optima_quinta")
    private Double optimoQuinta;

    @Column(name = "minima_quinta")
    private Double minimoQuinta;

    @Column(name = "maxima_quinta")
    private Double maximoQuinta;

    @Column(name = "optima_sexta")
    private Double optimoSexta;

    @Column(name = "minima_sexta")
    private Double minimoSexta;

    @Column(name = "maxima_sexta")
    private Double maximoSexta;

    @Column(name = "optima_septima")
    private Double optimoSeptima;

    @Column(name = "minima_septima")
    private Double minimoSeptima;

    @Column(name = "maxima_septima")
    private Double maximoSeptima;

    @Column(name = "optima_octava")
    private Double optimoOctava;

    @Column(name = "minima_octava")
    private Double minimoOctava;

    @Column(name = "maxima_octava")
    private Double maximoOctava;

    @Column(name = "optima_novena")
    private Double optimoNovena;

    @Column(name = "minima_novena")
    private Double minimoNovena;

    @Column(name = "maxima_novena")
    private Double maximoNovena;

    @Column(name = "optima_decima")
    private Double optimoDecima;

    @Column(name = "minima_decima")
    private Double minimoDecima;

    @Column(name = "maxima_decima")
    private Double maximoDecima;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "velocidadProgramada", cascade = CascadeType.REMOVE)
    private TablaMaestraServicios tablaMaestraServicios;


    public VelocidadProgramada() {
    }

    public VelocidadProgramada(Double[] valores) {
        this.optimoPrimera = valores[0];
        this.minimoPrimera = valores[1];
        this.maximoPrimera = valores[2];
        this.optimoSegunda = valores[3];
        this.minimoSegunda = valores[4];
        this.maximoSegunda = valores[5];
        this.optimoTercera = valores[6];
        this.minimoTercera = valores[7];
        this.maximoTercera = valores[8];
        this.optimoCuarta = valores[9];
        this.minimoCuarta = valores[10];
        this.maximoCuarta = valores[11];
        this.optimoQuinta = valores[12];
        this.minimoQuinta = valores[13];
        this.maximoQuinta = valores[14];
        this.optimoSexta = valores[15];
        this.minimoSexta = valores[16];
        this.maximoSexta = valores[17];
        this.optimoSeptima = valores[18];
        this.minimoSeptima = valores[19];
        this.maximoSeptima = valores[20];
        this.optimoOctava = valores[21];
        this.minimoOctava = valores[22];
        this.maximoOctava = valores[23];
        this.optimoNovena = valores[24];
        this.minimoNovena = valores[25];
        this.maximoNovena = valores[26];
        this.optimoDecima = valores[27];
        this.minimoDecima = valores[28];
        this.maximoDecima = valores[29];

    }

    public Double getOptimoPrimera() {
        return optimoPrimera;
    }

    public void setOptimoPrimera(Double optimoPrimera) {
        this.optimoPrimera = optimoPrimera;
    }

    public Double getMinimoPrimera() {
        return minimoPrimera;
    }

    public void setMinimoPrimera(Double minimoPrimera) {
        this.minimoPrimera = minimoPrimera;
    }

    public Double getMaximoPrimera() {
        return maximoPrimera;
    }

    public void setMaximoPrimera(Double maximoPrimera) {
        this.maximoPrimera = maximoPrimera;
    }

    public Double getOptimoSegunda() {
        return optimoSegunda;
    }

    public void setOptimoSegunda(Double optimoSegunda) {
        this.optimoSegunda = optimoSegunda;
    }

    public Double getMinimoSegunda() {
        return minimoSegunda;
    }

    public void setMinimoSegunda(Double minimoSegunda) {
        this.minimoSegunda = minimoSegunda;
    }

    public Double getMaximoSegunda() {
        return maximoSegunda;
    }

    public void setMaximoSegunda(Double maximoSegunda) {
        this.maximoSegunda = maximoSegunda;
    }

    public Double getOptimoTercera() {
        return optimoTercera;
    }

    public void setOptimoTercera(Double optimoTercera) {
        this.optimoTercera = optimoTercera;
    }

    public Double getMinimoTercera() {
        return minimoTercera;
    }

    public void setMinimoTercera(Double minimoTercera) {
        this.minimoTercera = minimoTercera;
    }

    public Double getMaximoTercera() {
        return maximoTercera;
    }

    public void setMaximoTercera(Double maximoTercera) {
        this.maximoTercera = maximoTercera;
    }

    public Double getOptimoCuarta() {
        return optimoCuarta;
    }

    public void setOptimoCuarta(Double optimoCuarta) {
        this.optimoCuarta = optimoCuarta;
    }

    public Double getMinimoCuarta() {
        return minimoCuarta;
    }

    public void setMinimoCuarta(Double minimoCuarta) {
        this.minimoCuarta = minimoCuarta;
    }

    public Double getMaximoCuarta() {
        return maximoCuarta;
    }

    public void setMaximoCuarta(Double maximoCuarta) {
        this.maximoCuarta = maximoCuarta;
    }

    public Double getOptimoQuinta() {
        return optimoQuinta;
    }

    public void setOptimoQuinta(Double optimoQuinta) {
        this.optimoQuinta = optimoQuinta;
    }

    public Double getMinimoQuinta() {
        return minimoQuinta;
    }

    public void setMinimoQuinta(Double minimoQuinta) {
        this.minimoQuinta = minimoQuinta;
    }

    public Double getMaximoQuinta() {
        return maximoQuinta;
    }

    public void setMaximoQuinta(Double maximoQuinta) {
        this.maximoQuinta = maximoQuinta;
    }

    public Double getOptimoSexta() {
        return optimoSexta;
    }

    public void setOptimoSexta(Double optimoSexta) {
        this.optimoSexta = optimoSexta;
    }

    public Double getMinimoSexta() {
        return minimoSexta;
    }

    public void setMinimoSexta(Double minimoSexta) {
        this.minimoSexta = minimoSexta;
    }

    public Double getMaximoSexta() {
        return maximoSexta;
    }

    public void setMaximoSexta(Double maximoSexta) {
        this.maximoSexta = maximoSexta;
    }

    public Double getOptimoSeptima() {
        return optimoSeptima;
    }

    public void setOptimoSeptima(Double optimoSeptima) {
        this.optimoSeptima = optimoSeptima;
    }

    public Double getMinimoSeptima() {
        return minimoSeptima;
    }

    public void setMinimoSeptima(Double minimoSeptima) {
        this.minimoSeptima = minimoSeptima;
    }

    public Double getMaximoSeptima() {
        return maximoSeptima;
    }

    public void setMaximoSeptima(Double maximoSeptima) {
        this.maximoSeptima = maximoSeptima;
    }

    public Double getOptimoOctava() {
        return optimoOctava;
    }

    public void setOptimoOctava(Double optimoOctava) {
        this.optimoOctava = optimoOctava;
    }

    public Double getMinimoOctava() {
        return minimoOctava;
    }

    public void setMinimoOctava(Double minimoOctava) {
        this.minimoOctava = minimoOctava;
    }

    public Double getMaximoOctava() {
        return maximoOctava;
    }

    public void setMaximoOctava(Double maximoOctava) {
        this.maximoOctava = maximoOctava;
    }

    public Double getOptimoNovena() {
        return optimoNovena;
    }

    public void setOptimoNovena(Double optimoNovena) {
        this.optimoNovena = optimoNovena;
    }

    public Double getMinimoNovena() {
        return minimoNovena;
    }

    public void setMinimoNovena(Double minimoNovena) {
        this.minimoNovena = minimoNovena;
    }

    public Double getMaximoNovena() {
        return maximoNovena;
    }

    public void setMaximoNovena(Double maximoNovena) {
        this.maximoNovena = maximoNovena;
    }

    public Double getOptimoDecima() {
        return optimoDecima;
    }

    public void setOptimoDecima(Double optimoDecima) {
        this.optimoDecima = optimoDecima;
    }

    public Double getMinimoDecima() {
        return minimoDecima;
    }

    public void setMinimoDecima(Double minimoDecima) {
        this.minimoDecima = minimoDecima;
    }

    public Double getMaximoDecima() {
        return maximoDecima;
    }

    public void setMaximoDecima(Double maximoDecima) {
        this.maximoDecima = maximoDecima;
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
