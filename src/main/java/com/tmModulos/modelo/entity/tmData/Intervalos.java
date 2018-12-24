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

    //Prueba

    @Column(name = "tipo_calculo")
    private String tipoCalculo;

    @Column(name = "valor_primera")
    private Double valorPrimera;

    @Column(name = "valor_segunda")
    private Double valorSegunda;

    @Column(name = "valor_tercera")
    private Double valorTercera;

    @Column(name = "valor_cuarta")
    private Double valorCuarta;

    @Column(name = "valor_quinta")
    private Double valorQuinta;

    @Column(name = "valor_sexta")
    private Double valorSexta;

    @Column(name = "valor_septima")
    private Double valorSeptima;

    @Column(name = "valor_octava")
    private Double valorOctava;

    @Column(name = "valor_novena")
    private Double valorNovena;

    @Column(name = "valor_decima")
    private Double valorDecima;

    @Column(name = "buses_primera")
    private int busesPrimera;

    @Column(name = "buses_segunda")
    private int busesSegunda;

    @Column(name = "buses_tercera")
    private int busesTercera;

    @Column(name = "buses_cuarta")
    private int busesCuarta;

    @Column(name = "buses_quinta")
    private int busesQuinta;

    @Column(name = "buses_sexta")
    private int busesSexta;

    @Column(name = "buses_septima")
    private int busesSeptima;

    @Column(name = "buses_octava")
    private int busesOctava;

    @Column(name = "buses_novena")
    private int busesNovena;

    @Column(name = "buses_decima")
    private int busesDecima;


    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "identificador", nullable = false)
    private ServicioTipoDia idServicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tabla_maestra", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TablaMaestraServicios tablaMaestraServicios;

    public Intervalos(String tipoCalculo, Double valorPrimera, Double valorSegunda, Double valorTercera, Double valorCuarta, Double valorQuinta, Double valorSexta, Double valorSeptima, Double valorOctava, Double valorNovena, Double valorDecima, ServicioTipoDia idServicio,TablaMaestraServicios tablaMaestraServicios) {
        this.tipoCalculo = tipoCalculo;
        this.valorPrimera = valorPrimera;
        this.valorSegunda = valorSegunda;
        this.valorTercera = valorTercera;
        this.valorCuarta = valorCuarta;
        this.valorQuinta = valorQuinta;
        this.valorSexta = valorSexta;
        this.valorSeptima = valorSeptima;
        this.valorOctava = valorOctava;
        this.valorNovena = valorNovena;
        this.valorDecima = valorDecima;
        this.idServicio = idServicio;
        this.tablaMaestraServicios=tablaMaestraServicios;
    }



    public Intervalos() {
    }

    public Intervalos(String tipoCalculo, Double valorPrimera, Double valorSegunda, Double valorTercera, Double valorCuarta, Double valorQuinta, Double valorSexta, Double valorSeptima, Double valorOctava, Double valorNovena, Double valorDecima, int busesPrimera, int busesSegunda, int busesTercera, int busesCuarta, int busesQuinta, int busesSexta, int busesSeptima, int busesOctava, int busesNovena, int busesDecima, ServicioTipoDia idServicio, TablaMaestraServicios tablaMaestraServicios) {
        this.tipoCalculo = tipoCalculo;
        this.valorPrimera = valorPrimera;
        this.valorSegunda = valorSegunda;
        this.valorTercera = valorTercera;
        this.valorCuarta = valorCuarta;
        this.valorQuinta = valorQuinta;
        this.valorSexta = valorSexta;
        this.valorSeptima = valorSeptima;
        this.valorOctava = valorOctava;
        this.valorNovena = valorNovena;
        this.valorDecima = valorDecima;
        this.busesPrimera = busesPrimera;
        this.busesSegunda = busesSegunda;
        this.busesTercera = busesTercera;
        this.busesCuarta = busesCuarta;
        this.busesQuinta = busesQuinta;
        this.busesSexta = busesSexta;
        this.busesSeptima = busesSeptima;
        this.busesOctava = busesOctava;
        this.busesNovena = busesQuinta;
        this.busesDecima = busesQuinta;
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

    public Double getValorPrimera() {
        return valorPrimera;
    }

    public void setValorPrimera(Double valorPrimera) {
        this.valorPrimera = valorPrimera;
    }

    public Double getValorSegunda() {
        return valorSegunda;
    }

    public void setValorSegunda(Double valorSegunda) {
        this.valorSegunda = valorSegunda;
    }

    public Double getValorTercera() {
        return valorTercera;
    }

    public void setValorTercera(Double valorTercera) {
        this.valorTercera = valorTercera;
    }

    public Double getValorCuarta() {
        return valorCuarta;
    }

    public void setValorCuarta(Double valorCuarta) {
        this.valorCuarta = valorCuarta;
    }

    public Double getValorQuinta() {
        return valorQuinta;
    }

    public void setValorQuinta(Double valorQuinta) {
        this.valorQuinta = valorQuinta;
    }

    public Double getValorSexta() {
        return valorSexta;
    }

    public void setValorSexta(Double valorSexta) {
        this.valorSexta = valorSexta;
    }

    public Double getValorSeptima() {
        return valorSeptima;
    }

    public void setValorSeptima(Double valorSeptima) {
        this.valorSeptima = valorSeptima;
    }

    public Double getValorOctava() {
        return valorOctava;
    }

    public void setValorOctava(Double valorOctava) {
        this.valorOctava = valorOctava;
    }

    public Double getValorNovena() {
        return valorNovena;
    }

    public void setValorNovena(Double valorNovena) {
        this.valorNovena = valorNovena;
    }

    public Double getValorDecima() {
        return valorDecima;
    }

    public void setValorDecima(Double valorDecima) {
        this.valorDecima = valorDecima;
    }

    public int getBusesPrimera() {
        return busesPrimera;
    }

    public void setBusesPrimera(int busesPrimera) {
        this.busesPrimera = busesPrimera;
    }

    public int getBusesSegunda() {
        return busesSegunda;
    }

    public void setBusesSegunda(int busesSegunda) {
        this.busesSegunda = busesSegunda;
    }

    public int getBusesTercera() {
        return busesTercera;
    }

    public void setBusesTercera(int busesTercera) {
        this.busesTercera = busesTercera;
    }

    public int getBusesCuarta() {
        return busesCuarta;
    }

    public void setBusesCuarta(int busesCuarta) {
        this.busesCuarta = busesCuarta;
    }

    public int getBusesQuinta() {
        return busesQuinta;
    }

    public void setBusesQuinta(int busesQuinta) {
        this.busesQuinta = busesQuinta;
    }

    public int getBusesSexta() {
        return busesSexta;
    }

    public void setBusesSexta(int busesSexta) {
        this.busesSexta = busesSexta;
    }

    public int getBusesSeptima() {
        return busesSeptima;
    }

    public void setBusesSeptima(int busesSeptima) {
        this.busesSeptima = busesSeptima;
    }

    public int getBusesOctava() {
        return busesOctava;
    }

    public void setBusesOctava(int busesOctava) {
        this.busesOctava = busesOctava;
    }

    public int getBusesNovena() {
        return busesNovena;
    }

    public void setBusesNovena(int busesNovena) {
        this.busesNovena = busesNovena;
    }

    public int getBusesDecima() {
        return busesDecima;
    }

    public void setBusesDecima(int busesDecima) {
        this.busesDecima = busesDecima;
    }
}
