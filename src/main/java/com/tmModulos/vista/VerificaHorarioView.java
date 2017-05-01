package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.DataProcesorImpl;
import com.tmModulos.controlador.procesador.VerificacionHorarios;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.io.Serializable;

@ManagedBean(name="VerHorario")
@ViewScoped
public class VerificaHorarioView implements Serializable {


    @ManagedProperty("#{VerificacionHorario}")
    private VerificacionHorarios verificacionHorarios;

    private UploadedFile file;
    private String tipoDia;
    private String tipoVerificacion;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @PostConstruct
    public void init(){

    }


    public void upload() {
            if(file!=null){
                try {
                    verificacionHorarios.compararExpediciones(file.getFileName(),file.getInputstream(),tipoVerificacion,tipoDia);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public VerificacionHorarios getVerificacionHorarios() {
        return verificacionHorarios;
    }

    public void setVerificacionHorarios(VerificacionHorarios verificacionHorarios) {
        this.verificacionHorarios = verificacionHorarios;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getTipoVerificacion() {
        return tipoVerificacion;
    }

    public void setTipoVerificacion(String tipoVerificacion) {
        this.tipoVerificacion = tipoVerificacion;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }
}


