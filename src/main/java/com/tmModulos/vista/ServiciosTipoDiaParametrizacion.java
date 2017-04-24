package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.OrdenServiciosService;
import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.modelo.entity.tmData.Servicio;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="serviciosTipoDiaP")
@SessionScoped
public class ServiciosTipoDiaParametrizacion {

    @ManagedProperty("#{ServicioService}")
    private ServicioService servicioService;

    @ManagedProperty("#{OrdenServiciosService}")
    private OrdenServiciosService ordenServiciosService;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    private List<ServicioTipoDia> serviciosRecords;
    private List<ServicioTipoDia> filteredServiciosRecords;

    private List<Servicio> serviciosPrincipalesRecords;
    private String nombreEspecialServicio;

    private ServicioTipoDia selectedServicio;
    private ServicioTipoDia nuevoServicio;
    private boolean tablaVisible;
    private String tipoDiaSeleccionado;
    private TipoDia tipoDia;
    private UploadedFile ordenServiciosFile;


    public void inicio(){


    }

    private boolean ordenNoExiste() {

        int ordenNuevo = selectedServicio.getOrden();
        for(ServicioTipoDia servicioTipoDia: serviciosRecords){
            if(servicioTipoDia.getOrden()==ordenNuevo){
                if(servicioTipoDia.getIdentificador()!=selectedServicio.getIdentificador()){
                    return false;
                }
            }
        }
        return true;
    }


    public void eliminar(){
        if(selectedServicio!= null){
           servicioService.deleteServicioTipoDia(selectedServicio);
            addMessage(FacesMessage.SEVERITY_INFO,"Servicio Eliminado", "");
            serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);
        }

    }

    public void cancelar(){

    }

    public void crear(){
        if(nombreEspecialServicio!=null && nuevoServicio!=null){
                Servicio servicio = servicioService.getServicioByNombreEspecial(nombreEspecialServicio);
                nuevoServicio.setServicio(servicio);
                nuevoServicio.setIdentificador(servicio.getIdentificador());
                nuevoServicio.setTipoDia(tipoDia);
                servicioService.addServicio(nuevoServicio);
                addMessage(FacesMessage.SEVERITY_INFO,"Servicio Creado", "");
                serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);

        }else{
            System.out.println("Error");
        }


    }

    public void buscar(){
        if(tipoDiaSeleccionado.equals("TODOS")){
            tipoDia = servicioService.getTipoDia("HABIL");
            serviciosRecords = servicioService.getTServiciosAll();
        }else{
            tipoDia = servicioService.getTipoDia(tipoDiaSeleccionado);
            serviciosRecords = servicioService.getServiciosByTipoDia(tipoDia);
        }

        tablaVisible=true;
    }

    public void habilitarNuevo(){
        nuevoServicio= new ServicioTipoDia();
        nuevoServicio.setOrden(serviciosRecords.get(serviciosRecords.size()-1).getOrden()+1);
    }

    public void actualizarOrdenServicios(){

    }
    public void procesarOrdenServicios() {

        if(ordenServiciosFile.getSize()>0 && ordenServiciosFile.getContentType().equals("application/vnd.ms-excel")) {
            try {
                List<ServicioTipoDia> servicios =  ordenServiciosService.processDataFromFile(ordenServiciosFile.getFileName(),ordenServiciosFile.getInputstream(),tipoDia,serviciosRecords);
                if(servicios.size()>0){
                 updateServiciosTipoDia(servicios);

                    messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_ORDEN_SERVICIOS_ACTUALIZADO);
                    serviciosRecords= servicioService.getServiciosByTipoDia(tipoDia);
                }else{
                    messagesView.error(Messages.MENSAJE_FALLO,Messages.ACCION_FALLO_ARCHIVO);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateServiciosTipoDia(List<ServicioTipoDia> servicios) {

        for(ServicioTipoDia servicioTipoDia:servicios){
            servicioService.updateServicioTipoDia(servicioTipoDia);
        }

    }

    public void addMessage(FacesMessage.Severity severity , String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    @PostConstruct
    public void init() {
        serviciosPrincipalesRecords = servicioService.getServicioAll();
    }


    public List<Servicio> getServiciosPrincipalesRecords() {
        return serviciosPrincipalesRecords;
    }

    public void setServiciosPrincipalesRecords(List<Servicio> serviciosPrincipalesRecords) {
        this.serviciosPrincipalesRecords = serviciosPrincipalesRecords;
    }

    public String getNombreEspecialServicio() {
        return nombreEspecialServicio;
    }

    public void setNombreEspecialServicio(String nombreEspecialServicio) {
        this.nombreEspecialServicio = nombreEspecialServicio;
    }

    public String getTipoDiaSeleccionado() {
        return tipoDiaSeleccionado;
    }

    public void setTipoDiaSeleccionado(String tipoDiaSeleccionado) {
        this.tipoDiaSeleccionado = tipoDiaSeleccionado;
    }

    public ServicioService getServicioService() {
        return servicioService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    public List<ServicioTipoDia> getServiciosRecords() {
        return serviciosRecords;
    }

    public void setServiciosRecords(List<ServicioTipoDia> serviciosRecords) {
        this.serviciosRecords = serviciosRecords;
    }

    public List<ServicioTipoDia> getFilteredServiciosRecords() {
        return filteredServiciosRecords;
    }

    public void setFilteredServiciosRecords(List<ServicioTipoDia> filteredServiciosRecords) {
        this.filteredServiciosRecords = filteredServiciosRecords;
    }

    public ServicioTipoDia getSelectedServicio() {
        return selectedServicio;
    }

    public void setSelectedServicio(ServicioTipoDia selectedServicio) {
        this.selectedServicio = selectedServicio;
    }

    public ServicioTipoDia getNuevoServicio() {
        return nuevoServicio;
    }

    public void setNuevoServicio(ServicioTipoDia nuevoServicio) {
        this.nuevoServicio = nuevoServicio;
    }

    public boolean isTablaVisible() {
        return tablaVisible;
    }

    public void setTablaVisible(boolean tablaVisible) {
        this.tablaVisible = tablaVisible;
    }

    public UploadedFile getOrdenServiciosFile() {
        return ordenServiciosFile;
    }

    public void setOrdenServiciosFile(UploadedFile ordenServiciosFile) {
        this.ordenServiciosFile = ordenServiciosFile;
    }

    public OrdenServiciosService getOrdenServiciosService() {
        return ordenServiciosService;
    }

    public void setOrdenServiciosService(OrdenServiciosService ordenServiciosService) {
        this.ordenServiciosService = ordenServiciosService;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }
}
