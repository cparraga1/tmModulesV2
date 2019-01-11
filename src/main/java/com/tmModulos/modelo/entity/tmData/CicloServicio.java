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

    @Column(name = "optimo_primera")
    private String optimoPrimera;

    @Column(name = "minimo_primera")
    private String minimoPrimera;

    @Column(name = "maximo_primera")
    private String maximoPrimera;

    @Column(name = "optimo_segunda")
    private String optimoSegunda;

    @Column(name = "minimo_segunda")
    private String minimoSegunda;

    @Column(name = "maximo_segunda")
    private String maximoSegunda;

    @Column(name = "optimo_tercera")
    private String optimoTercera;

    @Column(name = "minimo_tercera")
    private String minimoTercera;

    @Column(name = "maximo_tercera")
    private String maximoTercera;

    @Column(name = "optimo_cuarta")
    private String optimoCuarta;

    @Column(name = "minimo_cuarta")
    private String minimoCuarta;

    @Column(name = "maximo_cuarta")
    private String maximoCuarta;

    @Column(name = "optimo_quinta")
    private String optimoQuinta;

    @Column(name = "minimo_quinta")
    private String minimoQuinta;

    @Column(name = "maximo_quinta")
    private String maximoQuinta;

    @Column(name = "optimo_sexta")
    private String optimoSexta;

    @Column(name = "minimo_sexta")
    private String minimoSexta;

    @Column(name = "maximo_sexta")
    private String maximoSexta;

    @Column(name = "optimo_septima")
    private String optimoSeptima;

    @Column(name = "minimo_septima")
    private String minimoSeptima;

    @Column(name = "maximo_septima")
    private String maximoSeptima;

    @Column(name = "optimo_octava")
    private String optimoOctava;

    @Column(name = "minimo_octava")
    private String minimoOctava;

    @Column(name = "maximo_octava")
    private String maximoOctava;

    @Column(name = "optimo_novena")
    private String optimoNovena;

    @Column(name = "minimo_novena")
    private String minimoNovena;

    @Column(name = "maximo_novena")
    private String maximoNovena;

    @Column(name = "optimo_decima")
    private String optimoDecima;

    @Column(name = "minimo_decima")
    private String minimoDecima;

    @Column(name = "maximo_decima")
    private String maximoDecima;



    @OneToOne(fetch = FetchType.LAZY, mappedBy = "cicloServicio", cascade = CascadeType.REMOVE)
    private TablaMaestraServicios tablaMaestraServicios;

    public CicloServicio(String optimoPrimera, String optimoSegunda, String optimoTercera, String optimoCuarta, String optimoQuinta, String optimoSexta, String optimoSeptima, String optimoOctava, String optimoNovena, String optimoDecima) {
        this.optimoPrimera = optimoPrimera;
        this.optimoSegunda = optimoSegunda;
        this.optimoTercera = optimoTercera;
        this.optimoCuarta = optimoCuarta;
        this.optimoQuinta = optimoQuinta;
        this.optimoSexta = optimoSexta;
        this.optimoSeptima = optimoSeptima;
        this.optimoOctava = optimoOctava;
        this.optimoNovena = optimoNovena;
        this.optimoDecima = optimoDecima;
    }

    public String getOptimoPrimera() {
        return optimoPrimera;
    }

    public void setOptimoPrimera(String optimoPrimera) {
        this.optimoPrimera = optimoPrimera;
    }

    public String getMinimoPrimera() {
        return minimoPrimera;
    }

    public void setMinimoPrimera(String minimoPrimera) {
        this.minimoPrimera = minimoPrimera;
    }

    public String getMaximoPrimera() {
        return maximoPrimera;
    }

    public void setMaximoPrimera(String maximoPrimera) {
        this.maximoPrimera = maximoPrimera;
    }

    public String getOptimoSegunda() {
        return optimoSegunda;
    }

    public void setOptimoSegunda(String optimoSegunda) {
        this.optimoSegunda = optimoSegunda;
    }

    public String getMinimoSegunda() {
        return minimoSegunda;
    }

    public void setMinimoSegunda(String minimoSegunda) {
        this.minimoSegunda = minimoSegunda;
    }

    public String getMaximoSegunda() {
        return maximoSegunda;
    }

    public void setMaximoSegunda(String maximoSegunda) {
        this.maximoSegunda = maximoSegunda;
    }

    public String getOptimoTercera() {
        return optimoTercera;
    }

    public void setOptimoTercera(String optimoTercera) {
        this.optimoTercera = optimoTercera;
    }

    public String getMinimoTercera() {
        return minimoTercera;
    }

    public void setMinimoTercera(String minimoTercera) {
        this.minimoTercera = minimoTercera;
    }

    public String getMaximoTercera() {
        return maximoTercera;
    }

    public void setMaximoTercera(String maximoTercera) {
        this.maximoTercera = maximoTercera;
    }

    public String getOptimoCuarta() {
        return optimoCuarta;
    }

    public void setOptimoCuarta(String optimoCuarta) {
        this.optimoCuarta = optimoCuarta;
    }

    public String getMinimoCuarta() {
        return minimoCuarta;
    }

    public void setMinimoCuarta(String minimoCuarta) {
        this.minimoCuarta = minimoCuarta;
    }

    public String getMaximoCuarta() {
        return maximoCuarta;
    }

    public void setMaximoCuarta(String maximoCuarta) {
        this.maximoCuarta = maximoCuarta;
    }

    public String getOptimoQuinta() {
        return optimoQuinta;
    }

    public void setOptimoQuinta(String optimoQuinta) {
        this.optimoQuinta = optimoQuinta;
    }

    public String getMinimoQuinta() {
        return minimoQuinta;
    }

    public void setMinimoQuinta(String minimoQuinta) {
        this.minimoQuinta = minimoQuinta;
    }

    public String getMaximoQuinta() {
        return maximoQuinta;
    }

    public void setMaximoQuinta(String maximoQuinta) {
        this.maximoQuinta = maximoQuinta;
    }

    public String getOptimoSexta() {
        return optimoSexta;
    }

    public void setOptimoSexta(String optimoSexta) {
        this.optimoSexta = optimoSexta;
    }

    public String getMinimoSexta() {
        return minimoSexta;
    }

    public void setMinimoSexta(String minimoSexta) {
        this.minimoSexta = minimoSexta;
    }

    public String getMaximoSexta() {
        return maximoSexta;
    }

    public void setMaximoSexta(String maximoSexta) {
        this.maximoSexta = maximoSexta;
    }

    public String getOptimoSeptima() {
        return optimoSeptima;
    }

    public void setOptimoSeptima(String optimoSeptima) {
        this.optimoSeptima = optimoSeptima;
    }

    public String getMinimoSeptima() {
        return minimoSeptima;
    }

    public void setMinimoSeptima(String minimoSeptima) {
        this.minimoSeptima = minimoSeptima;
    }

    public String getMaximoSeptima() {
        return maximoSeptima;
    }

    public void setMaximoSeptima(String maximoSeptima) {
        this.maximoSeptima = maximoSeptima;
    }

    public String getOptimoOctava() {
        return optimoOctava;
    }

    public void setOptimoOctava(String optimoOctava) {
        this.optimoOctava = optimoOctava;
    }

    public String getMinimoOctava() {
        return minimoOctava;
    }

    public void setMinimoOctava(String minimoOctava) {
        this.minimoOctava = minimoOctava;
    }

    public String getMaximoOctava() {
        return maximoOctava;
    }

    public void setMaximoOctava(String maximoOctava) {
        this.maximoOctava = maximoOctava;
    }

    public String getOptimoNovena() {
        return optimoNovena;
    }

    public void setOptimoNovena(String optimoNovena) {
        this.optimoNovena = optimoNovena;
    }

    public String getMinimoNovena() {
        return minimoNovena;
    }

    public void setMinimoNovena(String minimoNovena) {
        this.minimoNovena = minimoNovena;
    }

    public String getMaximoNovena() {
        return maximoNovena;
    }

    public void setMaximoNovena(String maximoNovena) {
        this.maximoNovena = maximoNovena;
    }

    public String getOptimoDecima() {
        return optimoDecima;
    }

    public void setOptimoDecima(String optimoDecima) {
        this.optimoDecima = optimoDecima;
    }

    public String getMinimoDecima() {
        return minimoDecima;
    }

    public void setMinimoDecima(String minimoDecima) {
        this.minimoDecima = minimoDecima;
    }

    public String getMaximoDecima() {
        return maximoDecima;
    }

    public void setMaximoDecima(String maximoDecima) {
        this.maximoDecima = maximoDecima;
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

    public TablaMaestraServicios getTablaMaestraServicios() {
        return tablaMaestraServicios;
    }

    public void setTablaMaestraServicios(TablaMaestraServicios tablaMaestraServicios) {
        this.tablaMaestraServicios = tablaMaestraServicios;
    }
}
