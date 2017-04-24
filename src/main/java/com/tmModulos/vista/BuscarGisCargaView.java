package com.tmModulos.vista;

import com.tmModulos.modelo.entity.tmData.ArcoTiempo;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.controlador.servicios.BusquedaService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="busquedaGis")
@SessionScoped
public class BuscarGisCargaView implements Serializable {

    private String busqueda;
    private String tipoDia;
    private String tipoFecha;
    private Date fechaInicial;
    private Date fechaFinal;
    private boolean visibleRecords;
    private boolean fechaFinalVisible;
    private String messageContent;


    private List<GisCarga> gisCargaRecords;
    private GisCarga selectedCarga;
    private List<GisCarga> selectedGisCarga;

    private List<ArcoTiempo> arcoTiempoRecords;
    private ArcoTiempo selectedArcoTiempo;
    private List<ArcoTiempo> filteredArcoTiempoRecords;

    @ManagedProperty("#{BusquedaService}")
    private BusquedaService busquedaService;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;


    public BuscarGisCargaView(){};

    @PostConstruct
    public void init() {
        busqueda = "1";
        tipoFecha = "fechaCreacion";
        fechaFinalVisible=false;
        gisCargaRecords = busquedaService.getGisCargaAll();
        selectedGisCarga = new ArrayList<>();
        visibleRecords = false;
    }


    public void reiniciar(){
        busqueda = "1";
        fechaFinalVisible=false;
        gisCargaRecords = new ArrayList<>();
        selectedGisCarga = new ArrayList<>();
        visibleRecords = false;
        fechaFinal = null;
        fechaInicial = null;
    }

    public void buscar(){
        visibleRecords=true;
        if (busqueda.equals("1")){
            if(fechaInicial!=null && !tipoFecha.equals("")){
                gisCargaRecords = busquedaService.busquedaFecha(fechaInicial, tipoFecha);
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else if (busqueda.equals("2")){
            if( fechaInicial!= null && fechaFinal!=null && !tipoFecha.equals("")){
                gisCargaRecords = busquedaService.busquedaRangos( fechaInicial,fechaFinal,tipoFecha );
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);
        }

    }

    public void atras(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/BuscarGISCarga.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void inicio(){

    }

    public void cambioTipoBusqueda(){
        if(busqueda.equals("1")){
            fechaFinalVisible= false;
        }else{
            fechaFinalVisible=true;
        }
    }


    public void busquedaCargaGis(){
        System.out.println("visible");
        visibleRecords=true;
        arcoTiempoRecords = busquedaService.busquedaArcos(selectedCarga);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/GISCargaTabla.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //  externalContext.redirect("foo.xhtml");
    }


    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<ArcoTiempo> getFilteredArcoTiempoRecords() {
        return filteredArcoTiempoRecords;
    }

    public void setFilteredArcoTiempoRecords(List<ArcoTiempo> filteredArcoTiempoRecords) {
        this.filteredArcoTiempoRecords = filteredArcoTiempoRecords;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getTipoFecha() {
        return tipoFecha;
    }

    public boolean isVisibleRecords() {
        return visibleRecords;
    }

    public void setVisibleRecords(boolean visibleRecords) {
        this.visibleRecords = visibleRecords;
    }

    public void setTipoFecha(String tipoFecha) {
        this.tipoFecha = tipoFecha;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public List<GisCarga> getGisCargaRecords() {
        return gisCargaRecords;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setGisCargaRecords(List<GisCarga> gisCargaRecords) {
        this.gisCargaRecords = gisCargaRecords;
    }

    public GisCarga getSelectedCarga() {
        return selectedCarga;
    }

    public void setSelectedCarga(GisCarga selectedCarga) {
        this.selectedCarga = selectedCarga;
    }

    public List<GisCarga> getSelectedGisCarga() {
        return selectedGisCarga;
    }

    public void setSelectedGisCarga(List<GisCarga> selectedGisCarga) {
        this.selectedGisCarga = selectedGisCarga;
    }

    public List<ArcoTiempo> getArcoTiempoRecords() {
        return arcoTiempoRecords;
    }

    public void setArcoTiempoRecords(List<ArcoTiempo> arcoTiempoRecords) {
        this.arcoTiempoRecords = arcoTiempoRecords;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }

    public BusquedaService getBusquedaService() {
        return busquedaService;
    }

    public void setBusquedaService(BusquedaService busquedaService) {
        this.busquedaService = busquedaService;
    }

    public boolean isFechaFinalVisible() {
        return fechaFinalVisible;
    }

    public void setFechaFinalVisible(boolean fechaFinalVisible) {
        this.fechaFinalVisible = fechaFinalVisible;
    }


    public ArcoTiempo getSelectedArcoTiempo() {
        return selectedArcoTiempo;
    }

    public void setSelectedArcoTiempo(ArcoTiempo selectedArcoTiempo) {
        this.selectedArcoTiempo = selectedArcoTiempo;
    }
}
