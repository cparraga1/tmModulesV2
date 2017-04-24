package com.tmModulos.vista;

import com.tmModulos.modelo.entity.tmData.DistanciaNodos;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="busquedaMatriz")
@SessionScoped
public class BusquedaMatrizDistanciaView implements Serializable {

    private String busqueda;
    private Date fechaInicial;
    private Date fechaFinal;
    private String tipoFecha;
    private boolean visibleRecords;
    private boolean fechaFinalVisible;

    private List<MatrizDistancia> MatrizDistanciaRecords;
    private MatrizDistancia selectedMatriz;
    private List<MatrizDistancia> selectedMatrizDistancia;

    private List<DistanciaNodos> distanciaNodosRecords;
    private List<DistanciaNodos> selectedDistanciaNodosRecords;
    private List<DistanciaNodos> filteredDistanciaNodosRecords;

    @ManagedProperty("#{MatrizDistanciaService}")
    private MatrizDistanciaService matrizDistanciaService;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    public BusquedaMatrizDistanciaView() {
    }

    @PostConstruct
    public void init() {
        busqueda = "1";
        fechaFinalVisible=false;
        MatrizDistanciaRecords = matrizDistanciaService.getMatrizDistanciaAll();
        selectedMatrizDistancia = new ArrayList<>();
        visibleRecords = false;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void inicio(){

    }

    public void reinciar(){
        busqueda = "1";
        fechaFinalVisible=false;
        MatrizDistanciaRecords = new ArrayList<>();
        selectedMatrizDistancia = new ArrayList<>();
        visibleRecords = false;
        fechaFinal = null;
        fechaInicial = null;
    }
    public void buscar(){
        visibleRecords=true;
        if (busqueda.equals("1")){
            if(fechaInicial!=null && tipoFecha!= null){
                MatrizDistanciaRecords = matrizDistanciaService.getMatrizDistanciaByFecha(tipoFecha,fechaInicial);
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else if (busqueda.equals("2")){
            if( fechaInicial!= null && fechaFinal!=null && tipoFecha!= null){
                MatrizDistanciaRecords = matrizDistanciaService.getMatrizDistanciaBetwenFechas(tipoFecha, fechaInicial,fechaFinal );
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);
        }

    }

    public void cambioTipoBusqueda(){
        if(busqueda.equals("1")){
            fechaFinalVisible= false;
        }else{
            fechaFinalVisible=true;
        }
    }


    public void atras(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/BuscarMatrizDistancia.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void busquedaMatrizDistancia(){
        visibleRecords=true;
        distanciaNodosRecords = matrizDistanciaService.getDistanciaNodosByMatriz(selectedMatriz);
        selectedDistanciaNodosRecords = new ArrayList<>();
        filteredDistanciaNodosRecords = new ArrayList<>();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/MatrizDistanciaTabla.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void eliminar(){
        for(DistanciaNodos distanciaNodos:selectedDistanciaNodosRecords){
            matrizDistanciaService.deleteDistanciaNodos(distanciaNodos);


        }
        messagesView.error(Messages.MENSAJE_EXITOSO,Messages.ACCION_ELIMINACION_REGISTROS);
        busquedaMatrizDistancia();
    }



    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getTipoFecha() {
        return tipoFecha;
    }

    public void setTipoFecha(String tipoFecha) {
        this.tipoFecha = tipoFecha;
    }

    public boolean isFechaFinalVisible() {
        return fechaFinalVisible;
    }

    public void setFechaFinalVisible(boolean fechaFinalVisible) {
        this.fechaFinalVisible = fechaFinalVisible;
    }

    public List<MatrizDistancia> getMatrizDistanciaRecords() {
        return MatrizDistanciaRecords;
    }

    public void setMatrizDistanciaRecords(List<MatrizDistancia> matrizDistanciaRecords) {
        MatrizDistanciaRecords = matrizDistanciaRecords;
    }

    public MatrizDistancia getSelectedMatriz() {
        return selectedMatriz;
    }

    public void setSelectedMatriz(MatrizDistancia selectedMatriz) {
        this.selectedMatriz = selectedMatriz;
    }

    public List<MatrizDistancia> getSelectedMatrizDistancia() {
        return selectedMatrizDistancia;
    }

    public void setSelectedMatrizDistancia(List<MatrizDistancia> selectedMatrizDistancia) {
        this.selectedMatrizDistancia = selectedMatrizDistancia;
    }

    public List<DistanciaNodos> getDistanciaNodosRecords() {
        if(distanciaNodosRecords == null){
            distanciaNodosRecords =matrizDistanciaService.getDistanciaNodosByMatriz(selectedMatriz);
        }
        return distanciaNodosRecords;
    }

    public void setDistanciaNodosRecords(List<DistanciaNodos> distanciaNodosRecords) {
        this.distanciaNodosRecords = distanciaNodosRecords;
    }

    public List<DistanciaNodos> getFilteredDistanciaNodosRecords() {
        return filteredDistanciaNodosRecords;
    }

    public void setFilteredDistanciaNodosRecords(List<DistanciaNodos> filteredDistanciaNodosRecords) {
        this.filteredDistanciaNodosRecords = filteredDistanciaNodosRecords;
    }

    public boolean isVisibleRecords() {
        return visibleRecords;
    }

    public void setVisibleRecords(boolean visibleRecords) {
        this.visibleRecords = visibleRecords;
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

    public MatrizDistanciaService getMatrizDistanciaService() {
        return matrizDistanciaService;
    }

    public void setMatrizDistanciaService(MatrizDistanciaService matrizDistanciaService) {
        this.matrizDistanciaService = matrizDistanciaService;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public List<DistanciaNodos> getSelectedDistanciaNodosRecords() {
        return selectedDistanciaNodosRecords;
    }

    public void setSelectedDistanciaNodosRecords(List<DistanciaNodos> selectedDistanciaNodosRecords) {
        this.selectedDistanciaNodosRecords = selectedDistanciaNodosRecords;
    }
}
