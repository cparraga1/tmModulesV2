package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="ServicioGenerator")
    @SequenceGenerator(name="ServicioGenerator", sequenceName = "servicio_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "macro")
    private int macro;

    @Column(name = "linea")
    private int linea;

    @Column(name = "seccion")
    private int seccion;

    @Column(name = "config")
    private int config;

    @Column(name = "punto")
    private int punto;

    @Column(name = "identificador")
    private String identificador;

    @Column(name = "nombre_general")
    private String nombreGeneral;

    @Column(name = "nombre_especial")
    private String nombreEspecial;

    @Column(name = "trayecto")
    private Integer trayecto;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "linea_compuesta")
    private Integer lineaCompuesta;

    @Column(name = "tipo_servicio")
    private String tipoServicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipologia", nullable = false)
    private Tipologia tipologia;


//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicio")
//    private Set<ServicioFranjaHorario> servicioFranjaRecords= new HashSet<ServicioFranjaHorario>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicio")
    private Set<ServicioTipoDia> servicioTipoDias = new HashSet<ServicioTipoDia>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "servicio")
    private Set<Horario> horario = new HashSet<Horario>(0);




    @Transient
    private String estadoFormato;

    @Transient
    private boolean habil;

    @Transient
    private boolean festivo;

    @Transient
    private boolean sabado;


    public Servicio() {
    }

    public Servicio(int macro, int linea, int seccion, int config) {
        this.macro = macro;
        this.linea = linea;
        this.seccion = seccion;
        this.config = config;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMacro() {
        return macro;
    }

    public void setMacro(int macro) {
        this.macro = macro;
    }

    public int getLinea() {
        return linea;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getNombreGeneral() {
        return nombreGeneral;
    }

    public void setNombreGeneral(String nombreGeneral) {
        this.nombreGeneral = nombreGeneral;
    }

    public String getNombreEspecial() {
        return nombreEspecial;
    }

    public void setNombreEspecial(String nombreEspecial) {
        this.nombreEspecial = nombreEspecial;
    }

    public Integer getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(Integer trayecto) {
        this.trayecto = trayecto;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Integer getLineaCompuesta() {
        return lineaCompuesta;
    }

    public void setLineaCompuesta(Integer lineaCompuesta) {
        this.lineaCompuesta = lineaCompuesta;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }


    public int getPunto() {
        return punto;
    }

    public void setPunto(int punto) {
        this.punto = punto;
    }



    public String getEstadoFormato() {
        if(estado){
            return "Activo";
        }
        return "Inactivo";
    }

    public void setEstadoFormato(String estadoFormato) {
        this.estadoFormato = estadoFormato;
    }

    public Set<ServicioTipoDia> getServicioTipoDias() {
        return servicioTipoDias;
    }

    public void setServicioTipoDias(Set<ServicioTipoDia> servicioTipoDias) {
        this.servicioTipoDias = servicioTipoDias;
    }


    public boolean isHabil() {
//        for (ServicioTipoDia servicio: servicioTipoDias ) {
//            if(servicio.getTipoDia().getNombre().equals("HABIL")){
//                return true;
//            }
//        }
        return false;
    }

    public void setHabil(boolean habil) {
        this.habil = habil;
    }

    public boolean isFestivo() {
//        for (ServicioTipoDia servicio: servicioTipoDias ) {
//            if(servicio.getTipoDia().getNombre().equals("FESTIVO")){
//                return true;
//            }
//        }
        return false;
    }

    public void setFestivo(boolean festivo) {
        this.festivo = festivo;
    }

    public boolean isSabado() {
//        for (ServicioTipoDia servicio: servicioTipoDias ) {
//            if(servicio.getTipoDia().getNombre().equals("SABADO")){
//                return true;
//            }
//        }
        return false;
    }

    public Set<Horario> getHorario() {
        return horario;
    }

    public void setHorario(Set<Horario> horario) {
        this.horario = horario;
    }

    public void setSabado(boolean sabado) {
        this.sabado = sabado;
    }
}
