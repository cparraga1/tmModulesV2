package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.modelo.entity.tmData.ListaNegraMatriz;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean(name="listaNegra")
@SessionScoped
public class ListaNegraMatrizView {

    @ManagedProperty("#{MatrizDistanciaService}")
    private MatrizDistanciaService matrizDistanciaService;


    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    private List<ListaNegraMatriz> listaNegraMatrizRecords;
    private List<ListaNegraMatriz> filteredListaNegraMatrizRecords;
    private ListaNegraMatriz selectedListaNegra;

    public ListaNegraMatrizView() {
    }

    @PostConstruct
    public void init() {
        listaNegraMatrizRecords = matrizDistanciaService.getListaNegraMatrizAll();
    }


    public void eliminar(){
        matrizDistanciaService.deleteListaNegraMatriz(selectedListaNegra);
        addMessage(FacesMessage.SEVERITY_INFO,"Servicio Eliminado de la lista negra", "");
        listaNegraMatrizRecords = matrizDistanciaService.getListaNegraMatrizAll();
    }


    public void addMessage(FacesMessage.Severity severity , String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
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

    public List<ListaNegraMatriz> getListaNegraMatrizRecords() {
        return listaNegraMatrizRecords;
    }

    public void setListaNegraMatrizRecords(List<ListaNegraMatriz> listaNegraMatrizRecords) {
        this.listaNegraMatrizRecords = listaNegraMatrizRecords;
    }

    public ListaNegraMatriz getSelectedListaNegra() {
        return selectedListaNegra;
    }

    public void setSelectedListaNegra(ListaNegraMatriz selectedListaNegra) {
        this.selectedListaNegra = selectedListaNegra;
    }

    public List<ListaNegraMatriz> getFilteredListaNegraMatrizRecords() {
        return filteredListaNegraMatrizRecords;
    }

    public void setFilteredListaNegraMatrizRecords(List<ListaNegraMatriz> filteredListaNegraMatrizRecords) {
        this.filteredListaNegraMatrizRecords = filteredListaNegraMatrizRecords;
    }
}
