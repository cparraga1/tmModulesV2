package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.IntervalosProcessor;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="nuevoIntervalos")
@ViewScoped
public class NuevoIntervalosGISView implements Serializable {


    @ManagedProperty("#{IntervalosProcessor}")
    private IntervalosProcessor intervalosProcessor;

    private Date fechaProgramacion;
    private Date fechaVigencia;
    private String descripcion;

    private List<String> tipoDia;
    private String selectedTipoDia;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;




    public NuevoIntervalosGISView() {
    }

    @PostConstruct
    public void init() {
        tipoDia = new ArrayList<>();
        tipoDia.add("HABIL");
        tipoDia.add("SABADO");
        tipoDia.add("FESTIVO");
    }

    public void generar(){
        if(fechaVigencia!=null && descripcion!=null){
           // intervalosProcessor.generarIntervalos(fechaVigencia,descripcion,selectedTipoDia);
        }

    }

    public IntervalosProcessor getIntervalosProcessor() {
        return intervalosProcessor;
    }

    public void setIntervalosProcessor(IntervalosProcessor intervalosProcessor) {
        this.intervalosProcessor = intervalosProcessor;
    }

    public Date getFechaProgramacion() {
        return fechaProgramacion;
    }

    public void setFechaProgramacion(Date fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public List<String> getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(List<String> tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getSelectedTipoDia() {
        return selectedTipoDia;
    }

    public void setSelectedTipoDia(String selectedTipoDia) {
        this.selectedTipoDia = selectedTipoDia;
    }
}
