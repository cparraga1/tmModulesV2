package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.ConfVeriHorario;
import com.tmModulos.modelo.entity.tmData.Horario;
import com.tmModulos.modelo.entity.tmData.TipoDia;
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

    private List<TipoDia> tipoDiaRecords;
    private TipoDia nuevoTipoDia;
    private TipoDia selectedTipoDia;
    private TipoDia duplicadoTipoDia;
    private String aDuplicar;

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
        nuevoTipoDia = new TipoDia();
    }

    public void habilitarDuplicado(){
        duplicadoTipoDia = new TipoDia();
    }

    public void eliminar(){

        if(selectedTipoDia!= null){
            List<Horario> horarios = confVeriHorario.getHorariosTipoDia(selectedTipoDia);
            if(horarios.size()>0){
                messagesView.error(Messages.MENSAJE_FALLO,"No se puede eliminar, el tipo día se encuentra asociado a uno o varios horarios");

            }else{
                confVeriHorario.deleteVerificacionTipoDia(selectedTipoDia);
                messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ELIMINADO);
                tipoDiaRecords = confVeriHorario.getTipoDiaAll();
            }

        }

    }

    public void reiniciar(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ParametrizacionVerificacionHorario.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actualizar (){
        if(selectedTipoDia!= null){
            confVeriHorario.updateVerificacionTipoDia(selectedTipoDia);
            messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ACTUALIZADO);
            tipoDiaRecords = confVeriHorario.getTipoDiaAll();
        }

    }

    public void duplicar(){
        if(aDuplicar!=null && duplicadoTipoDia.getNombre()!=null){
            TipoDia nuevoTipoDia = confVeriHorario.getTipoDia(duplicadoTipoDia.getNombre());
            if(nuevoTipoDia==null){
                TipoDia tipoDia = confVeriHorario.getTipoDia(aDuplicar);
                if(tipoDia!=null){
                    confVeriHorario.duplicarTipoDia(duplicadoTipoDia,tipoDia);
                    messagesView.info(Messages.MENSAJE_EXITOSO,"Tipo Dia duplicado");
                }
            }else{
                messagesView.error(Messages.MENSAJE_FALLO,"Existe un tipo día con el mismo nombre");
            }

        }
    }

    public void cancelar (){
        nuevoTipoDia = new TipoDia();
        selectedTipoDia = null;
        duplicadoTipoDia = new TipoDia();
    }

    public void nuevoTipo(){
            if(nuevoTipoDia!=null){
                TipoDia tipoDia = confVeriHorario.getTipoDia(nuevoTipoDia.getNombre());
                if(tipoDia==null){
                    confVeriHorario.addVerificacionTipoDia(nuevoTipoDia);
                    messagesView.info(Messages.MENSAJE_EXITOSO,Messages.ACCION_TIPO_DIA_ALMACENADA);
                    tipoDiaRecords = confVeriHorario.getTipoDiaAll();
                }else{
                    messagesView.error(Messages.MENSAJE_FALLO,"Existe un tipo día con el mismo nombre");
                }

            }
    }

    public ConfVeriHorario getConfVeriHorario() {
        return confVeriHorario;
    }

    public void setConfVeriHorario(ConfVeriHorario confVeriHorario) {
        this.confVeriHorario = confVeriHorario;
    }

    public List<TipoDia> getTipoDiaRecords() {
        return tipoDiaRecords;
    }

    public void setTipoDiaRecords(List<TipoDia> tipoDiaRecords) {
        this.tipoDiaRecords = tipoDiaRecords;
    }

    public TipoDia getNuevoTipoDia() {
        return nuevoTipoDia;
    }

    public void setNuevoTipoDia(TipoDia nuevoTipoDia) {
        this.nuevoTipoDia = nuevoTipoDia;
    }

    public TipoDia getSelectedTipoDia() {
        return selectedTipoDia;
    }

    public void setSelectedTipoDia(TipoDia selectedTipoDia) {
        this.selectedTipoDia = selectedTipoDia;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public TipoDia getDuplicadoTipoDia() {
        return duplicadoTipoDia;
    }

    public void setDuplicadoTipoDia(TipoDia duplicadoTipoDia) {
        this.duplicadoTipoDia = duplicadoTipoDia;
    }

    public String getaDuplicar() {
        return aDuplicar;
    }

    public void setaDuplicar(String aDuplicar) {
        this.aDuplicar = aDuplicar;
    }
}
