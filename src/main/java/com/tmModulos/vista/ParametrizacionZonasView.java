package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.ConfZonas;
import com.tmModulos.modelo.entity.tmData.Zona;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
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
            try{
                confZonas.deleteZona(zonaSeleccionada);
                messagesView.info(Messages.MENSAJE_EXITOSO,"Zona Eliminada");
                zonas = confZonas.getAllZonas();
                reload();
            }catch (Exception e){
                messagesView.error(Messages.MENSAJE_FALLO,"Verifique que la zona no este asociada a ninguna estación");
            }


        }

    }

    public void actualizar (){
        if(zonaSeleccionada!= null){
            if (!zonaSeleccionada.getNombre().equals("") && (zonaSeleccionada.getTipoZona().equals("P")||
                    zonaSeleccionada.getTipoZona().equals("U"))){
                confZonas.updateZona(zonaSeleccionada);
                messagesView.info(Messages.MENSAJE_EXITOSO,"Zona actualizada");
                zonas = confZonas.getAllZonas();
            }else{
                messagesView.error(Messages.MENSAJE_FALLO,"Verifique los datos y vuelva a intentarlo");
            }

        }

    }

    public void cancelar (){
        nuevaZona = new Zona();
        zonaSeleccionada = null;
    }

    public void crearNuevaZona(){
        if(nuevaZona!=null){
            if (!nuevaZona.getNombre().equals("") && (nuevaZona.getTipoZona().equals("P")||
                    nuevaZona.getTipoZona().equals("U"))){
                confZonas.addZona(nuevaZona);
                messagesView.info(Messages.MENSAJE_EXITOSO,"Nueva Zona Almacenada");
                zonas = confZonas.getAllZonas();
            }else{
                messagesView.error(Messages.MENSAJE_FALLO,"Verifique los datos y vuelva a intentarlo");
            }

        }
    }

    public void reload(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ParametrizarZonas.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
