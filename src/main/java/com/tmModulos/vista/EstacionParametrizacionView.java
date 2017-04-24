package com.tmModulos.vista;

import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.NodoService;
import com.tmModulos.modelo.entity.tmData.*;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(name="nodosP")
@SessionScoped
public class EstacionParametrizacionView implements Serializable {


    @ManagedProperty("#{NodoService}")
    private NodoService nodoService;

    private List<Estacion> estacionRecords;
    private List<Estacion> selectedestacionRecords;
    private Estacion selectedestacion;
    private List<Estacion> filteredestacionRecords;

    private List<Zona> zonasProgramadasRecords;
    private String auxNombreZonaP;


    private String auxNombreZonaU;
    private List<Zona> zonasUsuariosRecords;

    private Nodo nuevoNodo;
    private String zonaProgramacion;
    private String zonaUsuario;

    private List<VagonesNodo> vagonesPorEstacion;
    private Nodo nuevoNodoVagon;
    private Nodo selectedNodo;
    private Vagon nuevoVagon;
    private Long vagonAEliminar;
    private Estacion nuevaEstacion;

    private String habilitarNuevo;
    private String eliminar;


    public EstacionParametrizacionView() {
    }

    @PostConstruct
    public void init() {
       estacionRecords= nodoService.getEstacionAll();
        selectedestacionRecords=new ArrayList<>();
        zonasProgramadasRecords = nodoService.getZonaByTipoZona("P");
        zonasUsuariosRecords = nodoService.getZonaByTipoZona("U");
    }

    public void inicio(){
    }

    public void habilitarNuevo(){
        nuevaEstacion = new Estacion();
    }

    public void onRowEdit(RowEditEvent event) {
        Estacion estacion = (Estacion) event.getObject();
        Zona zonaP = nodoService.getZonaByName(auxNombreZonaP,"P");
        estacion.setZonaProgramacion(zonaP);
        Zona zonaU = nodoService.getZonaByName(auxNombreZonaU,"U");
        estacion.setZonaUsuario(zonaU);
        nodoService.updateEstacion(estacion);
        FacesMessage msg = new FacesMessage("Zona Actualizada", (estacion).getNombre());
        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void onRowCancel(RowEditEvent event) {
        System.out.println();
    }


    public void editarNodo(Nodo nodoSelecionado){
        selectedNodo =nodoSelecionado;
    }
    public void editarNodosVagon(){
        if(selectedNodo != null) {
            nodoService.updateNodo(selectedNodo);
            addMessage(FacesMessage.SEVERITY_INFO,"Nodo Editado", "");

        }else{
            addMessage(FacesMessage.SEVERITY_ERROR,"Nodo no fue editado", "Verifique los campos");
        }
    }
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {
        }
    }

    public void verDetalleEstacion(){
        vagonesPorEstacion = calcularVagonesyNodos();
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/VagonNodoParametrizacion.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void añadirNodo(Vagon vagon){
        nuevoNodoVagon = new Nodo();
        nuevoNodoVagon.setVagon(vagon);
    }

    public void añadirVagon(){
        nuevoVagon = new Vagon();
    }



    public void eliminarVagon(){
        if(vagonAEliminar!=null){
            Vagon vagon= nodoService.getVagonbyId(vagonAEliminar);
            nodoService.deleteVagon(vagon);
            addMessage(FacesMessage.SEVERITY_INFO,"Vagon Eliminado", "");
            verDetalleEstacion();
        }else{
            addMessage(FacesMessage.SEVERITY_ERROR,"El Vagon no se pudo eliminar", "");
        }
    }

    public void añadirNuevoVagon(){
        if(nuevoVagon!=null){
            nuevoVagon.setEstacion(selectedestacion);
            nodoService.addVagon(nuevoVagon);
            addMessage(FacesMessage.SEVERITY_INFO,"Vagon Creado", "");
            vagonesPorEstacion = calcularVagonesyNodos();
        }else{
            addMessage(FacesMessage.SEVERITY_ERROR,"El Vagon no fue creado", "Complete los campos");
        }
    }

    public void atras(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/NodosParametrizacion.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<VagonesNodo> calcularVagonesyNodos() {
        List<VagonesNodo> dataNodosVagones = new ArrayList<VagonesNodo>();
       List<Vagon> vagones = nodoService.getVagonbyEstacion(selectedestacion);
       for(Vagon vagon:vagones){
           List<Nodo> nodos = nodoService.getNodoByVagon(vagon);
           VagonesNodo  vagonesNodo = new VagonesNodo(vagon,nodos);
           dataNodosVagones.add(vagonesNodo);
       }
        return dataNodosVagones;
    }

    public void añadirNodoVagon(){
        if(nuevoNodoVagon.getCodigo()!=null && nuevoNodoVagon.getNombre()!=null){
           nodoService.addNodo(nuevoNodoVagon);
           addMessage(FacesMessage.SEVERITY_INFO,"Nodo Creado", "");
           vagonesPorEstacion = calcularVagonesyNodos();
        }else{
            addMessage(FacesMessage.SEVERITY_ERROR,"El nodo no fue creado", "Complete los campos");
        }
    }

    public void eliminarNodo(){
        nodoService.deleteNodo(selectedNodo);
        addMessage(FacesMessage.SEVERITY_INFO,"Nodo Eliminado", "");
        verDetalleEstacion();
    }


    public void nuevo(){
        if(nuevaEstacion.getCodigo()!=null && nuevaEstacion.getNombre()!=null){
            Zona zonaP = nodoService.getZonaByName(auxNombreZonaP,"P");
            nuevaEstacion.setZonaProgramacion(zonaP);
            Zona zonaU = nodoService.getZonaByName(auxNombreZonaU,"U");
            nuevaEstacion.setZonaUsuario(zonaU);
            nodoService.addEstacion(nuevaEstacion);
            addMessage(FacesMessage.SEVERITY_INFO,"Estacion Creado", "");
            estacionRecords= nodoService.getEstacionAll();
            selectedestacionRecords=new ArrayList<>();
        }else{
            addMessage(FacesMessage.SEVERITY_ERROR,"La estacion no fue creado", "Complete los campos");
        }

    }

    public void cancelar(){

    }

    public void eliminarEstacion(){
        for(Estacion estacion:selectedestacionRecords){
            nodoService.deleteEstacion(estacion);
        }
        addMessage(FacesMessage.SEVERITY_INFO,"Nodo Eliminado", "");
        redirectNodos();
    }

    public void redirectNodos(){
        estacionRecords= nodoService.getEstacionAll();
        zonasProgramadasRecords = nodoService.getZonaByTipoZona("P");
        zonasUsuariosRecords = nodoService.getZonaByTipoZona("U");
        selectedestacionRecords=new ArrayList<>();
        selectedestacion= null;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/NodosParametrizacion.xhtml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void addMessage(FacesMessage.Severity severity , String summary, String detail) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }



    public NodoService getNodoService() {
        return nodoService;
    }

    public void setNodoService(NodoService nodoService) {
        this.nodoService = nodoService;
    }

    public List<Zona> getZonasProgramadasRecords() {
        return zonasProgramadasRecords;
    }

    public void setZonasProgramadasRecords(List<Zona> zonasProgramadasRecords) {
        this.zonasProgramadasRecords = zonasProgramadasRecords;
    }

    public String getAuxNombreZonaP() {
        return auxNombreZonaP;
    }

    public void setAuxNombreZonaP(String auxNombreZonaP) {
        this.auxNombreZonaP = auxNombreZonaP;
    }

    public String getAuxNombreZonaU() {
        return auxNombreZonaU;
    }

    public void setAuxNombreZonaU(String auxNombreZonaU) {
        this.auxNombreZonaU = auxNombreZonaU;
    }

    public List<Zona> getZonasUsuariosRecords() {
        return zonasUsuariosRecords;
    }

    public void setZonasUsuariosRecords(List<Zona> zonasUsuariosRecords) {
        this.zonasUsuariosRecords = zonasUsuariosRecords;
    }



    public Nodo getNuevoNodo() {
        return nuevoNodo;
    }

    public void setNuevoNodo(Nodo nuevoNodo) {
        this.nuevoNodo = nuevoNodo;
    }

    public String getZonaProgramacion() {
        return zonaProgramacion;
    }

    public void setZonaProgramacion(String zonaProgramacion) {
        this.zonaProgramacion = zonaProgramacion;
    }

    public String getZonaUsuario() {
        return zonaUsuario;
    }

    public void setZonaUsuario(String zonaUsuario) {
        this.zonaUsuario = zonaUsuario;
    }

    public List<Estacion> getEstacionRecords() {
        return estacionRecords;
    }

    public void setEstacionRecords(List<Estacion> estacionRecords) {
        this.estacionRecords = estacionRecords;
    }

    public List<Estacion> getSelectedestacionRecords() {
        return selectedestacionRecords;
    }

    public void setSelectedestacionRecords(List<Estacion> selectedestacionRecords) {
        this.selectedestacionRecords = selectedestacionRecords;
    }



    public List<Estacion> getFilteredestacionRecords() {
        return filteredestacionRecords;
    }

    public void setFilteredestacionRecords(List<Estacion> filteredestacionRecords) {
        this.filteredestacionRecords = filteredestacionRecords;
    }

    public Estacion getSelectedestacion() {
        return selectedestacion;
    }

    public void setSelectedestacion(Estacion selectedestacion) {
        this.selectedestacion = selectedestacion;
    }







    public List<VagonesNodo> getVagonesPorEstacion() {
        return vagonesPorEstacion;
    }

    public void setVagonesPorEstacion(List<VagonesNodo> vagonesPorEstacion) {
        this.vagonesPorEstacion = vagonesPorEstacion;
    }

    public Nodo getNuevoNodoVagon() {
        return nuevoNodoVagon;
    }

    public void setNuevoNodoVagon(Nodo nuevoNodoVagon) {
        this.nuevoNodoVagon = nuevoNodoVagon;
    }

    public Nodo getSelectedNodo() {
        return selectedNodo;
    }

    public void setSelectedNodo(Nodo selectedNodo) {
        this.selectedNodo = selectedNodo;
    }

    public Vagon getNuevoVagon() {
        return nuevoVagon;
    }

    public void setNuevoVagon(Vagon nuevoVagon) {
        this.nuevoVagon = nuevoVagon;
    }

    public Long getVagonAEliminar() {
        return vagonAEliminar;
    }

    public void setVagonAEliminar(Long vagonAEliminar) {
        this.vagonAEliminar = vagonAEliminar;
    }

    public Estacion getNuevaEstacion() {
        return nuevaEstacion;
    }

    public void setNuevaEstacion(Estacion nuevaEstacion) {
        this.nuevaEstacion = nuevaEstacion;
    }

    public String getEliminar() {
        return eliminar;
    }

    public void setEliminar(String eliminar) {
        this.eliminar = eliminar;
    }
}
