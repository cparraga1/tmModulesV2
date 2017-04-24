package com.tmModulos.modelo.entity.saeBogota;

import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
//@Table(name="VistaHorarioFecha",schema="dbo")
@Table(name="Horario",schema="dbo")
public class HorarioS implements Serializable {

    @Id
    @Column(name = "TipoDia")
    private String cuadro;
    @Id
    @Column(name = "Macro")
    private int macro;
    @Id
    @Column(name = "Linea")
    private int linea;
    @Id
    @Column(name = "Punto")
    private int punto;
    @Id
    @Column(name = "Tipo")
    private int tipo;
    @Id
    @Column(name = "ServBus")
    private String servBus;
    @Id
    @Column(name = "Instante")
    private int instante;
    @Column(name = "Evento")
    private int evento;
    @Column(name = "Sublinea")
    private int sublinea;
    @Id
    @Column(name = "Seccion")
    private int seccion;
    @Column(name = "Viaje")
    private int viaje;

//    @Formula(" CONVERT (CHAR(1), Instante / 36000)")
//    private String tiempo;
//
//    public String getTiempo() {
//        return tiempo;
//    }
//
//    public void setTiempo(String tiempo) {
//        this.tiempo = tiempo;
//    }

    //    @Id
//    @Column(name = "Hora")
//    private String hora;
//    @Id
//    @Column(name = "Fecha")
//    private Date fecha;

//
//    public String getHora() {
//        return hora;
//    }
//
//    public void setHora(String hora) {
//        this.hora = hora;
//    }
//
//    public Date getFecha() {
//        return fecha;
//    }
//
//    public void setFecha(Date fecha) {
//        this.fecha = fecha;
//    }

    public String getCuadro() {
        return cuadro;
    }

    public void setCuadro(String cuadro) {
        this.cuadro = cuadro;
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

    public int getPunto() {
        return punto;
    }

    public void setPunto(int punto) {
        this.punto = punto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getServBus() {
        return servBus;
    }

    public void setServBus(String servBus) {
        this.servBus = servBus;
    }

    public int getInstante() {
        return instante;
    }

    public void setInstante(int instante) {
        this.instante = instante;
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public int getSublinea() {
        return sublinea;
    }

    public void setSublinea(int sublinea) {
        this.sublinea = sublinea;
    }

    public int getSeccion() {
        return seccion;
    }

    public void setSeccion(int seccion) {
        this.seccion = seccion;
    }

    public int getViaje() {
        return viaje;
    }

    public void setViaje(int viaje) {
        this.viaje = viaje;
    }


}
