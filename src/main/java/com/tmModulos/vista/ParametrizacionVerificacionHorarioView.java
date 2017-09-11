package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.ConfVeriHorario;
import com.tmModulos.modelo.entity.tmData.VerificacionTipoDia;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

@ManagedBean(name="confVerif")
@ViewScoped
public class ParametrizacionVerificacionHorarioView {

    @ManagedProperty("#{ConfService}")
    private ConfVeriHorario confVeriHorario;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    private List<VerificacionTipoDia> tipoDiaRecords;
    private VerificacionTipoDia nuevoTipoDia;
    private VerificacionTipoDia selectedTipoDia;

    public ParametrizacionVerificacionHorarioView() {
    }

    @PostConstruct
    public void init() {
        tipoDiaRecords = confVeriHorario.getTipoDiaAll();
    }

    public void inicio(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void habilitarNuevo(){
        nuevoTipoDia = new VerificacionTipoDia();
    }

    public void eliminar(){

        if(selectedTipoDia!= null){
            confVeriHorario.deleteVerificacionTipoDia(selectedTipoDia);
            messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ELIMINADO);
            tipoDiaRecords = confVeriHorario.getTipoDiaAll();
        }

    }

    public void actualizar (){
        if(selectedTipoDia!= null){
            confVeriHorario.updateVerificacionTipoDia(selectedTipoDia);
            messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ACTUALIZADO);
            tipoDiaRecords = confVeriHorario.getTipoDiaAll();
        }

    }

    public void cancelar (){
        nuevoTipoDia = new VerificacionTipoDia();
        selectedTipoDia = null;
    }

    public void nuevoTipo(){
            if(nuevoTipoDia!=null){
                confVeriHorario.addVerificacionTipoDia(nuevoTipoDia);
                messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ALMACENADA);
                tipoDiaRecords = confVeriHorario.getTipoDiaAll();
            }
    }

    public ConfVeriHorario getConfVeriHorario() {
        return confVeriHorario;
    }

    public void setConfVeriHorario(ConfVeriHorario confVeriHorario) {
        this.confVeriHorario = confVeriHorario;
    }

    public List<VerificacionTipoDia> getTipoDiaRecords() {
        return tipoDiaRecords;
    }

    public void setTipoDiaRecords(List<VerificacionTipoDia> tipoDiaRecords) {
        this.tipoDiaRecords = tipoDiaRecords;
    }

    public VerificacionTipoDia getNuevoTipoDia() {
        return nuevoTipoDia;
    }

    public void setNuevoTipoDia(VerificacionTipoDia nuevoTipoDia) {
        this.nuevoTipoDia = nuevoTipoDia;
    }

    public VerificacionTipoDia getSelectedTipoDia() {
        return selectedTipoDia;
    }

    public void setSelectedTipoDia(VerificacionTipoDia selectedTipoDia) {
        this.selectedTipoDia = selectedTipoDia;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }
}
