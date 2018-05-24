package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.controlador.utils.TipoHorario;
import com.tmModulos.modelo.entity.tmData.*;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="serviciosP")
@ViewScoped
public class ServiciosParametrizacionView {


    @ManagedProperty("#{ServicioService}")
    private ServicioService servicioService;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    private List<Servicio> serviciosRecords;
    private List<Servicio> filteredServiciosRecords;
    private Servicio selectedServicio;
    private Servicio nuevoServicio;

    private List<String> tipologias;
    private List<String> selectedtipoDia;
    private List<String> tipoServicio;

    private List<Horario> horarioPorServicio;
    private List<TipoHorario> tipoHorarios;
    private List<TipoDia> tipodias;
    private String tipoDia;
    private Horario nuevoHorario;
    private Horario selectedHorario;
    private Date horaInicioNuevoHorario;
    private Date horaFinNuevoHorario;

    private String console;

    private boolean tipoDiaVisible;

    private List<String> availableItems;


    @PostConstruct
    public void init() {
        serviciosRecords= servicioService.getServicioAll();
        tipodias = servicioService.getTipoDiaAll();
        tipoHorarios = obtenerTipoHorarios();
        horarioPorServicio = new ArrayList<>();
        tipologias = new ArrayList<>();
        tipologias.add("ARTICULADO");
        tipologias.add("BIARTICULADO");
        tipologias.add("MIXTO");
        tipoServicio= new ArrayList<>();
        tipoServicio.add("1");
        tipoServicio.add("1-1");
        updateTipoDias();
    }

    private List<TipoHorario> obtenerTipoHorarios() {

        tipoHorarios = new ArrayList<TipoHorario>();
        tipoHorarios.add(new TipoHorario("Para Usuario","U"));
        tipoHorarios.add(new TipoHorario("Para Programacion","P"));

        return tipoHorarios;
    }

    private void updateTipoDias() {
        availableItems = new ArrayList<>();
        List<TipoDia> tipoDiaAll = servicioService.getTipoDiaAll();
        for(TipoDia tipoDia:tipoDiaAll){
            availableItems.add(tipoDia.getNombre());
        }
    }

    public ServiciosParametrizacionView() {
    }


    public void actualizar(){
        if(console==null || console.equals("")){
            console="ARTICULADO";
        }
        Tipologia tipologia = servicioService.getTipologiaByNombre(console);
        selectedServicio.setTipologia(tipologia);
        String identificador = selectedServicio.getMacro()+"-"+selectedServicio.getLinea()+"-"+selectedServicio.getSeccion()+"-"+selectedServicio.getPunto();
        String identificadorGIS = selectedServicio.getMacro()+"-"+selectedServicio.getSentido()+"-"+selectedServicio.getLinea();
        selectedServicio.setIdentificador(identificador);
        selectedServicio.setIdentificadorGIS(identificadorGIS);
        servicioService.updateServicio(selectedServicio);
        messagesView.info("Servicio Actualizado","");
        actualizarServicioPorTipoDia();
    }

    private void actualizarServicioPorTipoDia() {

        //selectedServicio.getServicioTipoDias();
    }

    private List<String> getTipoDiasPorServicio() {
        List<String> dias= new ArrayList<>();
        String id = selectedServicio.getIdentificador();
        if(id!=null){
            List<ServicioTipoDia> servicioTipoDias = servicioService.getServiciosById(selectedServicio.getIdentificador());
            for (ServicioTipoDia servicio:servicioTipoDias ) {
                dias.add(servicio.getTipoDia().getNombre());
            }
            return dias;
        }

        return new ArrayList<>();
    }

    public void eliminar(){
        selectedServicio.setEstado(false);
        servicioService.updateServicio(selectedServicio);
        List<ServicioTipoDia> serviciosByServicio = servicioService.getServiciosByServicio(selectedServicio);
        for(ServicioTipoDia servicioTipoDia:serviciosByServicio){
            servicioService.deleteServicioTipoDia(servicioTipoDia);
        }
        messagesView.info("Servicio Eliminado","");
        serviciosRecords = servicioService.getServicioAll();
    }

    public void addMessage(FacesMessage.Severity severity , String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }


    public void habilitarNuevo(){
        nuevoServicio= new Servicio();
        selectedtipoDia= new ArrayList<>();
    }

    public void nuevo(){
        if(console==null || console.equals("")){
            console="ARTICULADO";
        }
        Tipologia tipologia = servicioService.getTipologiaByNombre(console);
        nuevoServicio.setTipologia(tipologia);
        String identificador = nuevoServicio.getMacro()+"-"+nuevoServicio.getLinea()+"-"+nuevoServicio.getSeccion()+"-"+nuevoServicio.getPunto();
        String identificadorGIS = nuevoServicio.getMacro()+"-"+nuevoServicio.getSentido()+"-"+nuevoServicio.getLinea()+"-"+nuevoServicio.getPunto();
        nuevoServicio.setIdentificador(identificador);
        nuevoServicio.setLineaCompuesta(0);
        nuevoServicio.setTrayecto(0);
        nuevoServicio.setIdentificadorGIS(identificadorGIS);
        servicioService.addServicio(nuevoServicio);
        serviciosRecords= servicioService.getServicioAll();
        messagesView.info("Servicio Creado","");
    }

    public void verDetalleFranjaHorario(){



        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.getFlash().put("servicio",selectedServicio);
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/HorarioServicioParametrizacion.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void cancelar(){

    }



    public List<Servicio> getServiciosRecords() {
        return serviciosRecords;
    }

    public void setServiciosRecords(List<Servicio> serviciosRecords) {
        this.serviciosRecords = serviciosRecords;
    }

    public List<Servicio> getFilteredServiciosRecords() {
        return filteredServiciosRecords;
    }

    public void setFilteredServiciosRecords(List<Servicio> filteredServiciosRecords) {
        this.filteredServiciosRecords = filteredServiciosRecords;
    }

    public Servicio getSelectedServicio() {
        return selectedServicio;
    }

    public void setSelectedServicio(Servicio selectedServicio) {
        this.selectedServicio = selectedServicio;
    }


    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public List<String> getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(List<String> tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Servicio getNuevoServicio() {
        return nuevoServicio;
    }

    public void setNuevoServicio(Servicio nuevoServicio) {
        this.nuevoServicio = nuevoServicio;
    }

    public ServicioService getServicioService() {
        return servicioService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    public List<String> getTipologias() {
        return tipologias;
    }

    public void setTipologias(List<String> tipologias) {
        this.tipologias = tipologias;
    }

    public List<String> getSelectedtipoDia() {
        return selectedtipoDia;
    }

    public void setSelectedtipoDia(List<String> selectedtipoDia) {
        this.selectedtipoDia = selectedtipoDia;
    }

    public boolean isTipoDiaVisible() {
        return tipoDiaVisible;
    }

    public void setTipoDiaVisible(boolean tipoDiaVisible) {
        this.tipoDiaVisible = tipoDiaVisible;
    }

    public List<String> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(List<String> availableItems) {
        this.availableItems = availableItems;
    }

    public List<Horario> getHorarioPorServicio() {
        return horarioPorServicio;
    }

    public void setHorarioPorServicio(List<Horario> horarioPorServicio) {
        this.horarioPorServicio = horarioPorServicio;
    }

    public Horario getNuevoHorario() {
        return nuevoHorario;
    }

    public void setNuevoHorario(Horario nuevoHorario) {
        this.nuevoHorario = nuevoHorario;
    }

    public Horario getSelectedHorario() {
        return selectedHorario;
    }

    public void setSelectedHorario(Horario selectedHorario) {
        this.selectedHorario = selectedHorario;
    }

    public Date getHoraInicioNuevoHorario() {
        return horaInicioNuevoHorario;
    }

    public void setHoraInicioNuevoHorario(Date horaInicioNuevoHorario) {
        this.horaInicioNuevoHorario = horaInicioNuevoHorario;
    }

    public Date getHoraFinNuevoHorario() {
        return horaFinNuevoHorario;
    }

    public void setHoraFinNuevoHorario(Date horaFinNuevoHorario) {
        this.horaFinNuevoHorario = horaFinNuevoHorario;
    }

    public List<TipoHorario> getTipoHorarios() {
        return tipoHorarios;
    }

    public void setTipoHorarios(List<TipoHorario> tipoHorarios) {
        this.tipoHorarios = tipoHorarios;
    }

    public List<TipoDia> getTipodias() {
        return tipodias;
    }

    public void setTipodias(List<TipoDia> tipodias) {
        this.tipodias = tipodias;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }
}
