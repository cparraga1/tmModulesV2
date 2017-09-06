package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.FranjaHorarioService;
import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="franjaParametrizacion")
@SessionScoped
public class FranjaHorarioParametrizacionView implements Serializable {

    private List<TipoFranja> franjaRecords;
    private List<TipoFranja> franjaRecordsFiltered;
    private TipoFranja franjaSelected;
    private TipoFranja franjaNueva;

    @ManagedProperty("#{franjaHorarioService}")
    private FranjaHorarioService franjaHorarioService;

    @PostConstruct
    public void init() {
        franjaRecords = franjaHorarioService.getAllTipoFranja();
        franjaRecordsFiltered = new ArrayList<>();
    }

    public void inicio (){

    }

    public void eliminar(){

    }

    public void actualizar(){
      franjaHorarioService.updateTipoFranja(franjaSelected);
        franjaRecords = franjaHorarioService.getAllTipoFranja();
        franjaSelected = null;
    }

    public void cancelar () {
        franjaRecords = franjaHorarioService.getAllTipoFranja();
        franjaSelected = null;
    }

    public FranjaHorarioParametrizacionView() {
    }

    public List<TipoFranja> getFranjaRecords() {
        return franjaRecords;
    }

    public void setFranjaRecords(List<TipoFranja> franjaRecords) {
        this.franjaRecords = franjaRecords;
    }

    public List<TipoFranja> getFranjaRecordsFiltered() {
        return franjaRecordsFiltered;
    }

    public void setFranjaRecordsFiltered(List<TipoFranja> franjaRecordsFiltered) {
        this.franjaRecordsFiltered = franjaRecordsFiltered;
    }

    public TipoFranja getFranjaSelected() {
        return franjaSelected;
    }

    public void setFranjaSelected(TipoFranja franjaSelected) {
        this.franjaSelected = franjaSelected;
    }

    public TipoFranja getFranjaNueva() {
        return franjaNueva;
    }

    public void setFranjaNueva(TipoFranja franjaNueva) {
        this.franjaNueva = franjaNueva;
    }

    public FranjaHorarioService getFranjaHorarioService() {
        return franjaHorarioService;
    }

    public void setFranjaHorarioService(FranjaHorarioService franjaHorarioService) {
        this.franjaHorarioService = franjaHorarioService;
    }

    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("Car Selected", ((TipoFranja) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("Car UNSelected", ((TipoFranja) event.getObject()).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
