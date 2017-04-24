package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="arco_tiempo")
public class ArcoTiempo {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="ArcoGenerator")
    @SequenceGenerator(name="ArcoGenerator", sequenceName = "arco_tiempo_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "sentido")
    private int sentido;

    @Column(name = "secuencia")
    private int secuencia;

    @Column(name = "tipo_arco")
    private int tipoArco;

    @Column(name = "distancia")
    private Integer distancia;

    @Column(name = "hora_desde")
    private String horaDesde;

    @Column(name = "hora_hasta")
    private String horaHasta;

    @Column(name = "tiempo_minimo")
    private String tiempoMinimo;

    @Column(name = "tiempo_maximo")
    private String tiempoMaximo;

    @Column(name = "tiempo_optimo")
    private String tiempoOptimo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gis_carga", nullable = false)
    private GisCarga gisCargaArco;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servicio", nullable = false)
    private GisServicio servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_dia", nullable = false)
    private TipoDiaDetalle tipoDiaByArco;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "nodo_inicial", nullable = false)
//    private Nodo nodoInicial;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "nodo_final", nullable = false)
//    private Nodo nodoFinal;

    public ArcoTiempo() {
    }

    public ArcoTiempo(int sentido, int secuencia, int tipoArco, Integer distancia, String horaDesde, String horaHasta, String tiempoMinimo, String tiempoMaximo, String tiempoOptimo, GisCarga gisCargaArco, GisServicio servicio, TipoDiaDetalle tipoDiaByArco) {
        this.sentido = sentido;
        this.secuencia = secuencia;
        this.tipoArco = tipoArco;
        this.distancia = distancia;
        this.horaDesde = horaDesde;
        this.horaHasta = horaHasta;
        this.tiempoMinimo = tiempoMinimo;
        this.tiempoMaximo = tiempoMaximo;
        this.tiempoOptimo = tiempoOptimo;
        this.gisCargaArco = gisCargaArco;
        this.servicio = servicio;
        this.tipoDiaByArco = tipoDiaByArco;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSentido() {
        return sentido;
    }

    public void setSentido(int sentido) {
        this.sentido = sentido;
    }

    public int getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(int secuencia) {
        this.secuencia = secuencia;
    }

    public int getTipoArco() {
        return tipoArco;
    }

    public void setTipoArco(int tipoArco) {
        this.tipoArco = tipoArco;
    }

    public Integer getDistancia() {
        return distancia;
    }

    public void setDistancia(Integer distancia) {
        this.distancia = distancia;
    }

    public String getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(String horaDesde) {
        this.horaDesde = horaDesde;
    }

    public String getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(String horaHasta) {
        this.horaHasta = horaHasta;
    }

    public String getTiempoMinimo() {
        return tiempoMinimo;
    }

    public void setTiempoMinimo(String tiempoMinimo) {
        this.tiempoMinimo = tiempoMinimo;
    }

    public String getTiempoMaximo() {
        return tiempoMaximo;
    }

    public void setTiempoMaximo(String tiempoMaximo) {
        this.tiempoMaximo = tiempoMaximo;
    }

    public String getTiempoOptimo() {
        return tiempoOptimo;
    }

    public void setTiempoOptimo(String tiempoOptimo) {
        this.tiempoOptimo = tiempoOptimo;
    }

    public GisCarga getGisCargaArco() {
        return gisCargaArco;
    }

    public void setGisCargaArco(GisCarga gisCargaArco) {
        this.gisCargaArco = gisCargaArco;
    }

    public GisServicio getServicio() {
        return servicio;
    }

    public void setServicio(GisServicio servicio) {
        this.servicio = servicio;
    }

    public TipoDiaDetalle getTipoDiaByArco() {
        return tipoDiaByArco;
    }

    public void setTipoDiaByArco(TipoDiaDetalle tipoDiaByArco) {
        this.tipoDiaByArco = tipoDiaByArco;
    }


}
