package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.TablaMaestraProcessor;
import com.tmModulos.controlador.utils.LogDatos;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.GisIntervalos;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.TablaMaestraServicios;
import org.primefaces.model.UploadedFile;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="tablaMaestra")
@ViewScoped
public class NuevaTablaMaestra {

    private String tipoGeneracion;
    private String descripcion;
    private boolean archivoVisible;
    private boolean definitivaVisible;
    private boolean automaticoVisible;
    private Date fechaDeProgramacion;
    private Date fechaDeVigencia;
    private Date fechaDeCreacion;
    private UploadedFile tablaMaestra;
    private String seleccionGIS;

    private String gisCarga;
    private String selectedTipoDia;
    private List<String> tipoDia;

    private List<GisCarga> gisCargaList;
    private List<GisCarga> filteredGisCargaList;
    private GisCarga gisCargaSelected;

    private String matrizDistancia;
    private List<MatrizDistancia> matrizDistanciasList;
    private List<MatrizDistancia> filteredMatrizDistanciasList;
    private MatrizDistancia selectedMatrizDistancia;

    private List<LogDatos> logDatos;
    private boolean resultadosVisibles;
    private String tipoTabla;


    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @ManagedProperty("#{TablaMaestraProcessor}")
    private TablaMaestraProcessor tablaMaestraProcessor;


    @PostConstruct
    public void init() {
        tipoGeneracion = "2";
        automaticoVisible=false;
        archivoVisible=true;
        gisCargaList = tablaMaestraProcessor.getGisCargaList();
        matrizDistanciasList = tablaMaestraProcessor.getMatrizDistanciaAll();
            tipoDia = new ArrayList<>();
            tipoDia.add("HABIL");
            tipoDia.add("SABADO");
            tipoDia.add("FESTIVO");
    }

    public void copiarTablaMaestra(){
        if(descripcion!=null){
            if(fechaDeProgramacion!=null && fechaDeCreacion!=null){
                logDatos = tablaMaestraProcessor.copiarUltimaTablaMaestra(fechaDeProgramacion,descripcion,selectedTipoDia,fechaDeCreacion);
                resultadosVisibles=true;
                if(logDatos.size()>2){
                    messagesView.error(Messages.MENSAJE_CALCULO_FALLA,Messages.ACCION_VERIFICACION);
                }else{
                    messagesView.info(Messages.MENSAJE_CALCULO_EXITOSO,Messages.ACCION_TABLA_MAESTRA_ALMACENADA);
                }

            }
        }
    }

    public void calcularTablaMaestra(){
        if(valid()){
            logDatos=tablaMaestraProcessor.calcularTablaMaestra(fechaDeProgramacion,
                    descripcion,gisCarga,matrizDistancia,fechaDeVigencia,selectedTipoDia);
            resultadosVisibles=true;
            if(logDatos.size()>2){
                messagesView.info(Messages.MENSAJE_CALCULO_EXITOSO,Messages.ACCION_TABLA_MAESTRA_ALMACENADA);
            }

        }else{
            messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS, Messages.ACCION_CAMPOS_INCOMPLETOS);
        }

    }

    public void cambioTipoTabla(){
        if(tipoTabla.equals("1")){
            definitivaVisible=false;
        }else{
            definitivaVisible=true;
        }
    }

    public boolean valid(){
        if(descripcion!=null){
            if(fechaDeProgramacion!=null && fechaDeVigencia!=null && selectedTipoDia != null){
                if(gisCarga!=null && matrizDistancia!=null){
                    return true;
                }


            }
        }
        return false;
    }

    public void habilitarBusquedaGIS(){

    }

    public void eliminar(){

    }

    public void seleccionarGis(){
       gisCarga = gisCargaSelected.getDescripcion();

    }

    public void seleccionarMatriz(){
        matrizDistancia = selectedMatrizDistancia.getNumeracion();
    }


    public String getTipoGeneracion() {
        return tipoGeneracion;
    }

    public void setTipoGeneracion(String tipoGeneracion) {
        this.tipoGeneracion = tipoGeneracion;
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

    public UploadedFile getTablaMaestra() {
        return tablaMaestra;
    }

    public void setTablaMaestra(UploadedFile tablaMaestra) {
        this.tablaMaestra = tablaMaestra;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public List<GisCarga> getGisCargaList() {
        return gisCargaList;
    }

    public void setGisCargaList(List<GisCarga> gisCargaList) {
        this.gisCargaList = gisCargaList;
    }

    public TablaMaestraProcessor getTablaMaestraProcessor() {
        return tablaMaestraProcessor;
    }

    public void setTablaMaestraProcessor(TablaMaestraProcessor tablaMaestraProcessor) {
        this.tablaMaestraProcessor = tablaMaestraProcessor;
    }


    public List<MatrizDistancia> getMatrizDistanciasList() {
        return matrizDistanciasList;
    }

    public void setMatrizDistanciasList(List<MatrizDistancia> matrizDistanciasList) {
        this.matrizDistanciasList = matrizDistanciasList;
    }

    public String getMatrizDistancia() {
        return matrizDistancia;
    }

    public void setMatrizDistancia(String matrizDistancia) {
        this.matrizDistancia = matrizDistancia;
    }

    public String getGisCarga() {
        return gisCarga;
    }

    public void setGisCarga(String gisCarga) {
        this.gisCarga = gisCarga;
    }

    public String getSelectedTipoDia() {
        return selectedTipoDia;
    }

    public void setSelectedTipoDia(String selectedTipoDia) {
        this.selectedTipoDia = selectedTipoDia;
    }

    public List<String> getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(List<String> tipoDia) {
        this.tipoDia = tipoDia;
    }

    public List<GisCarga> getFilteredGisCargaList() {
        return filteredGisCargaList;
    }

    public void setFilteredGisCargaList(List<GisCarga> filteredGisCargaList) {
        this.filteredGisCargaList = filteredGisCargaList;
    }

    public GisCarga getGisCargaSelected() {
        return gisCargaSelected;
    }

    public void setGisCargaSelected(GisCarga gisCargaSelected) {
        this.gisCargaSelected = gisCargaSelected;
    }

    public String getSeleccionGIS() {
        return seleccionGIS;
    }

    public void setSeleccionGIS(String seleccionGIS) {
        this.seleccionGIS = seleccionGIS;
    }

    public List<MatrizDistancia> getFilteredMatrizDistanciasList() {
        return filteredMatrizDistanciasList;
    }

    public void setFilteredMatrizDistanciasList(List<MatrizDistancia> filteredMatrizDistanciasList) {
        this.filteredMatrizDistanciasList = filteredMatrizDistanciasList;
    }

    public MatrizDistancia getSelectedMatrizDistancia() {
        return selectedMatrizDistancia;
    }

    public void setSelectedMatrizDistancia(MatrizDistancia selectedMatrizDistancia) {
        this.selectedMatrizDistancia = selectedMatrizDistancia;
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

    public String getTipoTabla() {
        return tipoTabla;
    }

    public void setTipoTabla(String tipoTabla) {
        this.tipoTabla = tipoTabla;
    }

    public boolean isDefinitivaVisible() {
        return definitivaVisible;
    }

    public void setDefinitivaVisible(boolean definitivaVisible) {
        this.definitivaVisible = definitivaVisible;
    }

    public Date getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setFechaDeCreacion(Date fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }
}
