package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.ConfZonas;
import com.tmModulos.modelo.entity.tmData.Zona;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean(name="confZonas")
@ViewScoped
public class ParametrizacionZonasView
{

    private List<Zona> zonas;
    private Zona nuevaZona;
    private Zona zonaSeleccionada;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @ManagedProperty("#{ConfZonas}")
    private ConfZonas confZonas;

    public ParametrizacionZonasView() {
    }

    @PostConstruct
    public void init() {
        zonas = confZonas.getAllZonas();
    }

    public void habilitarNuevo(){
        nuevaZona = new Zona();
    }

    public void eliminar(){

        if(zonaSeleccionada!= null){
            confZonas.deleteZona(zonaSeleccionada);
            messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ELIMINADO);
            zonas = confZonas.getAllZonas();
        }

    }

    public void actualizar (){
        if(zonaSeleccionada!= null){
            confZonas.updateZona(zonaSeleccionada);
            messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ACTUALIZADO);
            zonas = confZonas.getAllZonas();
        }

    }

    public void cancelar (){
        nuevaZona = new Zona();
        zonaSeleccionada = null;
    }

    public void crearNuevaZona(){
        if(nuevaZona!=null){
            confZonas.addZona(nuevaZona);
            messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ALMACENADA);
            zonas = confZonas.getAllZonas();
        }
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public Zona getNuevaZona() {
        return nuevaZona;
    }

    public void setNuevaZona(Zona nuevaZona) {
        this.nuevaZona = nuevaZona;
    }

    public Zona getZonaSeleccionada() {
        return zonaSeleccionada;
    }

    public void setZonaSeleccionada(Zona zonaSeleccionada) {
        this.zonaSeleccionada = zonaSeleccionada;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public ConfZonas getConfZonas() {
        return confZonas;
    }

    public void setConfZonas(ConfZonas confZonas) {
        this.confZonas = confZonas;
    }
}
