package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.ConversionIntervalosServicio;
import com.tmModulos.controlador.servicios.HorariosProvisionalServicio;
import com.tmModulos.modelo.entity.tmData.GisIntervalos;
import com.tmModulos.modelo.entity.tmData.Intervalos;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="busquedaInt")
@SessionScoped
public class BuscarGisIntervalosView {

    private String busqueda;
    private String tipoDia;
    private String tipoFecha;
    private Date fechaInicial;
    private Date fechaFinal;
    private boolean visibleRecords;
    private boolean fechaFinalVisible;
    private String messageContent;


    private List<GisIntervalos> gisIntervalosRecords;
    private GisIntervalos selectedIntervalo;
    private List<GisIntervalos> selectedGisIntervalos;

    private List<Intervalos> intervalosRecords;
    private List<Intervalos> selectedIntervalosRecords;

    @ManagedProperty("#{HorariosProvisionalServicio}")
    private HorariosProvisionalServicio busquedaService;

    @ManagedProperty("#{ConversionIntervalosServicio}")
    private ConversionIntervalosServicio conversionIntervalosServicio;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    private String destination="C:\\temp\\";

    private StreamedContent file;

    public BuscarGisIntervalosView() {
    }

    @PostConstruct
    public void init() {
        busqueda = "1";
        fechaFinalVisible=false;
        gisIntervalosRecords = busquedaService.getGisIntervalosAll();
        selectedGisIntervalos = new ArrayList<>();
        visibleRecords = false;
    }


    public void reiniciar(){
        busqueda = "1";
        fechaFinalVisible=false;
        gisIntervalosRecords = new ArrayList<>();
        selectedGisIntervalos = new ArrayList<>();
        visibleRecords = false;
        fechaFinal = null;
        fechaInicial = null;
    }

    public void buscar(){
        visibleRecords=true;
        if (busqueda.equals("1")){
            if(fechaInicial!=null && tipoFecha!= null){
                gisIntervalosRecords = busquedaService.getGisIntervaloByFecha( tipoFecha,fechaInicial);
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else if (busqueda.equals("2")){
            if( fechaInicial!= null && fechaFinal!=null && tipoFecha!= null){
                gisIntervalosRecords = busquedaService.getGisIntervaloBetwenFechas( tipoFecha,fechaInicial,fechaFinal );
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);
        }

    }

    public void atras(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/BuscarGISIntervalos.xhtml");
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



    public void descargarArchivo(){

    }

    public void busquedaCargaGis(){
        System.out.println("visible");
        visibleRecords=true;
        intervalosRecords = busquedaService.getIntervalosByGisIntervalos(selectedIntervalo);
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/IntervalosTabla.xhtml");
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

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getTipoFecha() {
        return tipoFecha;
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

    public boolean isVisibleRecords() {
        return visibleRecords;
    }

    public void setVisibleRecords(boolean visibleRecords) {
        this.visibleRecords = visibleRecords;
    }

    public boolean isFechaFinalVisible() {
        return fechaFinalVisible;
    }

    public void setFechaFinalVisible(boolean fechaFinalVisible) {
        this.fechaFinalVisible = fechaFinalVisible;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public List<GisIntervalos> getGisIntervalosRecords() {
        return gisIntervalosRecords;
    }

    public void setGisIntervalosRecords(List<GisIntervalos> gisIntervalosRecords) {
        this.gisIntervalosRecords = gisIntervalosRecords;
    }

    public GisIntervalos getSelectedIntervalo() {
        return selectedIntervalo;
    }

    public void setSelectedIntervalo(GisIntervalos selectedIntervalo) {
        this.selectedIntervalo = selectedIntervalo;
    }

    public List<GisIntervalos> getSelectedGisIntervalos() {
        return selectedGisIntervalos;
    }

    public void setSelectedGisIntervalos(List<GisIntervalos> selectedGisIntervalos) {
        this.selectedGisIntervalos = selectedGisIntervalos;
    }

    public HorariosProvisionalServicio getBusquedaService() {
        return busquedaService;
    }

    public void setBusquedaService(HorariosProvisionalServicio busquedaService) {
        this.busquedaService = busquedaService;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public List<Intervalos> getIntervalosRecords() {
        return intervalosRecords;
    }

    public void setIntervalosRecords(List<Intervalos> intervalosRecords) {
        this.intervalosRecords = intervalosRecords;
    }

    public List<Intervalos> getSelectedIntervalosRecords() {
        return selectedIntervalosRecords;
    }

    public void setSelectedIntervalosRecords(List<Intervalos> selectedIntervalosRecords) {
        this.selectedIntervalosRecords = selectedIntervalosRecords;
    }

    public ConversionIntervalosServicio getConversionIntervalosServicio() {
        return conversionIntervalosServicio;
    }

    public void setConversionIntervalosServicio(ConversionIntervalosServicio conversionIntervalosServicio) {
        this.conversionIntervalosServicio = conversionIntervalosServicio;
    }

    public StreamedContent getFile() {
        file = conversionIntervalosServicio.crearArchivoDeIntervalos(selectedIntervalo);
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
}
