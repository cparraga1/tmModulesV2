package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.controlador.utils.TipoHorario;
import com.tmModulos.modelo.entity.tmData.Horario;
import com.tmModulos.modelo.entity.tmData.Servicio;
import com.tmModulos.modelo.entity.tmData.TipoDia;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="horarios")
@ViewScoped
public class HorariosView {

    private List<Horario> horarioPorServicio;
    private List<TipoHorario> tipoHorarios;
    private List<TipoDia> tipodias;
    private Servicio selectedServicio;
    private Servicio nuevoServicio;
    private Horario nuevoHorario;
    private Horario selectedHorario;
    private Date horaInicioNuevoHorario;
    private Date horaFinNuevoHorario;
    private String tipoDia;

    @ManagedProperty("#{ServicioService}")
    private ServicioService servicioService;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;


    public HorariosView() {
    }

    @PostConstruct
    public void init() {
        selectedServicio = (Servicio) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("servicio");
        horarioPorServicio = obtenerHorariosPorServicioSeleccionado();
        tipoHorarios = obtenerTipoHorarios();
        tipodias = servicioService.getTipoDiaAll();
    }

    private List<TipoHorario> obtenerTipoHorarios() {

        tipoHorarios = new ArrayList<TipoHorario>();
        tipoHorarios.add(new TipoHorario("Para Usuario","U"));
        tipoHorarios.add(new TipoHorario("Para Programacion","P"));

        return tipoHorarios;
    }

    public void crearNuevoHorario(){
        nuevoHorario = new Horario();
    }

    public void crearHorarioServicio(){
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        if(horaInicioNuevoHorario!=null && horaFinNuevoHorario!= null){
            /*if(horaInicioNuevoHorario.after(horaFinNuevoHorario)){
                messagesView.error("Error","El horario Inicio debe ser mayor que el horario Fin");
            }else{*/
                nuevoHorario.setHoraInicio(parser.format(horaInicioNuevoHorario));
                nuevoHorario.setHoraFin(parser.format(horaFinNuevoHorario));
                nuevoHorario.setServicio(selectedServicio);
                TipoDia tipoDiaObj = servicioService.getTipoDia(tipoDia);
                nuevoHorario.setTipoDia(tipoDiaObj);
                servicioService.addHorarios(nuevoHorario);
                horarioPorServicio = servicioService.getHorariosByServicio(selectedServicio);
            /*}*/
        }


    }

    public void actualizarHorarioServicio(){
        servicioService.updateHorarios(selectedHorario);
    }

    public void eliminarHorario(){
        if(selectedHorario!= null){
            servicioService.deleteHorarios(selectedHorario);
            verDetalleFranjaHorario();
        }
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

    public void atras(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ServiciosParametrizacion.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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

    public List<Horario> getHorarioPorServicio() {
        return horarioPorServicio;
    }

    public void setHorarioPorServicio(List<Horario> horarioPorServicio) {
        this.horarioPorServicio = horarioPorServicio;
    }

    public List<TipoHorario> getTipoHorarios() {
        return tipoHorarios;
    }

    public void setTipoHorarios(List<TipoHorario> tipoHorarios) {
        this.tipoHorarios = tipoHorarios;
    }

    public Servicio getSelectedServicio() {
        return selectedServicio;
    }

    public void setSelectedServicio(Servicio selectedServicio) {
        this.selectedServicio = selectedServicio;
    }

    public Servicio getNuevoServicio() {
        return nuevoServicio;
    }

    public void setNuevoServicio(Servicio nuevoServicio) {
        this.nuevoServicio = nuevoServicio;
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

    public ServicioService getServicioService() {
        return servicioService;
    }

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public List<TipoDia> getTipodias() {
        return tipodias;
    }

    public void setTipodias(List<TipoDia> tipodias) {
        this.tipodias = tipodias;
    }
}
