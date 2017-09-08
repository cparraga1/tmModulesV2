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
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="serviciosP")
@SessionScoped
public class ServiciosParametrizacionView {


    @ManagedProperty("#{ServicioService}")
    private ServicioService servicioService;

    private List<Servicio> serviciosRecords;
    private List<Servicio> filteredServiciosRecords;
    private Servicio selectedServicio;
    private Servicio nuevoServicio;

    private List<String> tipologias;
    private List<String> selectedtipoDia;
    private List<String> tipoServicio;

    private List<Horario> horarioPorServicio;
    private List<TipoHorario> tipoHorarios;
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

    public void inicio(){

    }

    public void actualizar(){
        if(console==null || console.equals("")){
            console="ARTICULADO";
        }
        Tipologia tipologia = servicioService.getTipologiaByNombre(console);
        selectedServicio.setTipologia(tipologia);
        String identificador = selectedServicio.getMacro()+"-"+selectedServicio.getLinea()+"-"+selectedServicio.getSeccion()+"-"+selectedServicio.getPunto();
        selectedServicio.setIdentificador(identificador);
        servicioService.updateServicio(selectedServicio);

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
        addMessage(FacesMessage.SEVERITY_INFO,"Servicio Eliminado", "");
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
        nuevoServicio.setIdentificador(identificador);
        servicioService.addServicio(nuevoServicio);
        serviciosRecords= servicioService.getServicioAll();
    }

    public void verDetalleFranjaHorario(){
        horarioPorServicio = obtenerHorariosPorServicioSeleccionado();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/HorarioServicioParametrizacion.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void atras(){

    }

    public void crearNuevoHorario(){
        nuevoHorario = new Horario();
    }

    public void crearHorarioServicio(){
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        nuevoHorario.setHoraInicio(parser.format(horaInicioNuevoHorario));
        nuevoHorario.setHoraFin(parser.format(horaFinNuevoHorario));
        nuevoHorario.setServicio(selectedServicio);
        servicioService.addHorarios(nuevoHorario);
        horarioPorServicio = servicioService.getHorariosByServicio(selectedServicio);

    }

    public void cancelarHorarioServicio(){
            nuevoHorario = new Horario();
            horaInicioNuevoHorario = null;
            horaFinNuevoHorario = null;
    }

    private List<Horario> obtenerHorariosPorServicioSeleccionado() {

        if(selectedServicio!= null) return servicioService.getHorariosByServicio(selectedServicio);

        return new ArrayList<Horario>();
    }

    public void cancelar(){

    }

    public ServicioService getServicioService() {
        return servicioService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
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

    public List<String> getTipologias() {
        return tipologias;
    }

    public void setTipologias(List<String> tipologias) {
        this.tipologias = tipologias;
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
}
