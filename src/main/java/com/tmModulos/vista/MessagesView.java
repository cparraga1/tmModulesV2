package com.tmModulos.vista;

import org.springframework.stereotype.Service;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Service("MessagesView")
public class MessagesView {

    public void info(String info, String accion) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, info,accion));
    }

    public void warn(String advertencia, String accion) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, advertencia,accion));
    }

    public void error(String error, String accion) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, error,accion));
    }

    public void fatal(String fatal, String accion) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, fatal,accion));
    }

}
