package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.MatrizProcessor;
import com.tmModulos.controlador.utils.ListObject;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.controlador.utils.ModosUtil;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@ManagedBean(name="matrizDis")
@ViewScoped
public class NuevaMatrizDistanciaView {


    private String tipoGeneracion;
    private String numeracion;
    private String descripcion;
    private boolean archivoVisible;
    private boolean automaticoVisible;
    private Date fechaDeProgramacion;
    private Date fechaDeVigencia;
    private Date fechaSabado;
    private Date fechaFestivos;
    private UploadedFile matrizDistancias;
    private List<LogDatos> logDatos;
    private boolean resultadosVisibles;
    private String formatoArchivo;

    private String tipoDia;
    private String modo;
    private List<ListObject> modos;
    private List<ListObject> tiposDia;


    @ManagedProperty("#{MatrizProcessor}")
    private MatrizProcessor matrizProcessor;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @PostConstruct
    public void init() {
        tipoGeneracion = "1";
        automaticoVisible=true;
        archivoVisible=false;
        fechaFestivos =null;
        fechaSabado=null;
        fechaDeVigencia=null;
        fechaDeProgramacion=null;
        resultadosVisibles=false;
        numeracion="";
        modos = ModosUtil.cargarListaModos();
        tiposDia = ModosUtil.cargarListaTipoDiaTroncal();
        formatoArchivo = "Nota: Recuerde que el archivo debe estar en Formato XLS (Excel 97-2003)";
    }

    public void cambioTipoGeneracion(){
        if(tipoGeneracion.equals("1")){
           automaticoVisible=true;
           archivoVisible=false;
        }else{
            automaticoVisible=false;
            archivoVisible=true;
        }
    }

    private void updateTipoDias(){
        if(modo.equals("DUA")){
            tiposDia = ModosUtil.cargarListaTipoDiaDual();
        }else{
            tiposDia = ModosUtil.cargarListaTipoDiaTroncal();
        }

    }

    public void cargarArchivo(){
        if(isValid()){
            if(matrizDistancias.getSize()>0 && matrizDistancias.getContentType().equals("application/vnd.ms-excel")) {
                try {
                    resultadosVisibles=true;
                    logDatos= matrizProcessor.processDataFromFile(matrizDistancias.getFileName(),matrizDistancias.getInputstream(), fechaDeProgramacion,numeracion,
                            fechaDeVigencia,fechaSabado,fechaFestivos,descripcion,modo);
                    if(matrizProcessor.isExitoso()){
                        messagesView.info(Messages.MENSAJE_CARGA_EXITOSA,Messages.ACCION_MATRIZ_ALMACENADA);
                    }else{
                        messagesView.error(Messages.MENSAJE_CALCULO_FALLA,Messages.ACCION_VERIFICACION);
                    }

                } catch (IOException e) {
                    messagesView.error(Messages.MENSAJE_FALLO_ARCHIVO,Messages.ACCION_FALLO_ARCHIVO);
                }

            }else{
                messagesView.error(Messages.MENSAJE_ARCHIVO_NO_EXCEL,Messages.ACCION_ARCHIVO_NO_EXCEL);
            }
        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);
        }


    }

    private boolean isValid() {

        if(matrizDistancias!=null){
            if( fechaDeVigencia!=null && fechaSabado!=null && fechaFestivos!=null){
                if(numeracion!=null){
                    return true;
                }
            }
        }
        return false;
    }



//    public void calcularMatrizDistancias(){
//        if( numeracion!=null ){
//            if(fechaDeVigencia!=null && fechaFestivos!= null && fechaSabado!=null){
//                   resultadosVisibles=true;
//                   logDatos= matrizProcessor.calcularMatrizDistancia(fechaDeVigencia,numeracion,fechaFestivos,fechaSabado,descripcion);
//                   if(logDatos.size()==2){
//                       messagesView.info(Messages.MENSAJE_CALCULO_EXITOSO,Messages.ACCION_MATRIZ_ALMACENADA);
//                   }else{
//                       messagesView.info(Messages.MENSAJE_CALCULO_REVISION,"");
//                   }
//            }else {
//                messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS, Messages.ACCION_CAMPOS_INCOMPLETOS);
//            }
//        }else{
//            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);
//        }
//
//    }


    public String getTipoGeneracion() {
        return tipoGeneracion;
    }

    public void setTipoGeneracion(String tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
    }

    public String getNumeracion() {
        return numeracion;
    }

    public void setNumeracion(String numeracion) {
        this.numeracion = numeracion;
    }

    public boolean isArchivoVisible() {
        return archivoVisible;
    }

    public void setArchivoVisible(boolean archivoVisible) {
        this.archivoVisible = archivoVisible;
    }

    public boolean isAutomaticoVisible() {
        return automaticoVisible;
    }

    public void setAutomaticoVisible(boolean automaticoVisible) {
        this.automaticoVisible = automaticoVisible;
    }

    public Date getFechaDeProgramacion() {
        return fechaDeProgramacion;
    }

    public void setFechaDeProgramacion(Date fechaDeProgramacion) {
        this.fechaDeProgramacion = fechaDeProgramacion;
    }

    public Date getFechaDeVigencia() {
        return fechaDeVigencia;
    }

    public void setFechaDeVigencia(Date fechaDeVigencia) {
        this.fechaDeVigencia = fechaDeVigencia;
    }

    public UploadedFile getMatrizDistancias() {
        return matrizDistancias;
    }

    public void setMatrizDistancias(UploadedFile matrizDistancias) {
        this.matrizDistancias = matrizDistancias;
    }

    public MatrizProcessor getMatrizProcessor() {
        return matrizProcessor;
    }

    public void setMatrizProcessor(MatrizProcessor matrizProcessor) {
        this.matrizProcessor = matrizProcessor;
    }


    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public Date getFechaSabado() {
        return fechaSabado;
    }

    public void setFechaSabado(Date fechaSabado) {
        this.fechaSabado = fechaSabado;
    }

    public Date getFechaFestivos() {
        return fechaFestivos;
    }

    public void setFechaFestivos(Date fechaFestivos) {
        this.fechaFestivos = fechaFestivos;
    }

    public List<LogDatos> getLogDatos() {
        return logDatos;
    }

    public void setLogDatos(List<LogDatos> logDatos) {
        this.logDatos = logDatos;
    }

    public boolean isResultadosVisibles() {
        return resultadosVisibles;
    }

    public void setResultadosVisibles(boolean resultadosVisibles) {
        this.resultadosVisibles = resultadosVisibles;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public List<ListObject> getModos() {
        return modos;
    }

    public void setModos(List<ListObject> modos) {
        this.modos = modos;
    }

    public List<ListObject> getTiposDia() {
        return tiposDia;
    }

    public void setTiposDia(List<ListObject> tiposDia) {
        this.tiposDia = tiposDia;
    }

    public String getFormatoArchivo() {
        return formatoArchivo;
    }

    public void setFormatoArchivo(String formatoArchivo) {
        this.formatoArchivo = formatoArchivo;
    }
}
