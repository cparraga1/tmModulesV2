package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.DataProcesorImpl;
import com.tmModulos.controlador.utils.LogDatos;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ManagedBean(name="gisCarga")
@ViewScoped
public class NuevoGisCargaView implements Serializable{

    @ManagedProperty("#{DataProcesorImpl}")
    private DataProcesorImpl dataProcesor;

    private Date fechaProgramacion;
    private Date fechaVigencia;
    private UploadedFile gisCarga;
    private String tipoDia;
    private String descripcion;
    private boolean status;
    private double progress = 0d;
    private List<LogDatos> logDatos;
    private boolean resultadosVisibles;


    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @PostConstruct
    public void init(){
        resultadosVisibles =false;
        fechaProgramacion = null;
        fechaVigencia = null;
        descripcion = "";
    }

    public void upload() {
        resultadosVisibles=false;
        if(isValid()){
        if(gisCarga.getSize()>0 && gisCarga.getContentType().equals("application/vnd.ms-excel")) {
            progress=20;
            try {
                status = true;
                if(descripcion.isEmpty()){ descripcion="Sin descripcion";}
                logDatos= dataProcesor.processDataFromFile(gisCarga.getFileName(),gisCarga.getInputstream(), fechaProgramacion, fechaVigencia,tipoDia,descripcion);
               status =false;
                if(dataProcesor.isExitoso()){
                    messagesView.info(Messages.MENSAJE_CARGA_EXITOSA,Messages.ACCION_GIS_CARGA_ALMACENADA);
                }else{
                    messagesView.error(Messages.MENSAJE_CALCULO_FALLA,Messages.ACCION_VERIFICACION);
                }

                resultadosVisibles=true;
            } catch (IOException e) {
                messagesView.error(Messages.MENSAJE_FALLO_ARCHIVO,Messages.ACCION_FALLO_ARCHIVO);
            }

        }else{
            messagesView.error(Messages.MENSAJE_FALLO_ARCHIVO,Messages.ACCION_ARCHIVO_INCOMPATIBLE);
        }
        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);
        }
    }

    private boolean isValid() {

        if(gisCarga!=null){
            if(fechaProgramacion!=null){
                if(fechaVigencia!=null){
                    if(tipoDia!= null){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isResultadosVisibles() {
        return resultadosVisibles;
    }

    public void setResultadosVisibles(boolean resultadosVisibles) {
        this.resultadosVisibles = resultadosVisibles;
    }

    public Date getFechaProgramacion() {
        return fechaProgramacion;
    }

    public void setFechaProgramacion(Date fechaProgramacion) {
        this.fechaProgramacion = fechaProgramacion;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public UploadedFile getGisCarga() {
        return gisCarga;
    }

    public void setGisCarga(UploadedFile gisCarga) {
        this.gisCarga = gisCarga;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }

    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");
    }


    public DataProcesorImpl getDataProcesor() {
        return dataProcesor;
    }

    public void setDataProcesor(DataProcesorImpl dataProcesor) {
        this.dataProcesor = dataProcesor;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public List<LogDatos> getLogDatos() {
        return logDatos;
    }

    public void setLogDatos(List<LogDatos> logDatos) {
        this.logDatos = logDatos;
    }
}
