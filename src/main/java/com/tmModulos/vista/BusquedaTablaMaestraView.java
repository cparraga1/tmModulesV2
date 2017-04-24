package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.TablaMaestraEdicion;
import com.tmModulos.controlador.procesador.TablaMaestraProcessor;
import com.tmModulos.controlador.servicios.MatrizDistanciaService;
import com.tmModulos.controlador.servicios.TablaMaestraService;
import com.tmModulos.controlador.utils.ProcessorUtils;
import com.tmModulos.modelo.entity.tmData.*;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ManagedBean(name="busquedaTabla")
@SessionScoped
public class BusquedaTablaMaestraView {

    private String busqueda;
    private Date fechaInicial;
    private Date fechaFinal;
    private String tipoFecha;
    private boolean visibleRecords;
    private boolean fechaFinalVisible;
    private boolean visibleOptimos;
    private boolean visibleMinimos;
    private boolean visibleMaximos;

//    private String tipologiaCiclo;
    private String tipoCiclo;
    private String tipoIntervalo;
    private String textoGeneracionMatrix;

    private List<TablaMaestra> tablaMaestraRecords;
    private TablaMaestra selectedTabla;
    private TablaMaestraServicios nuevaTabla;
    private List<TablaMaestra> filteredTablaMaestra;


    private List<TablaMaestraServicios> tServiciosRecords;
    private List<TablaMaestraServicios> selectedServiciosRecords;
    private List<TablaMaestraServicios> filteredTServiciosRecords;
    TablaMaestraServicios serviciosSelected;

    @ManagedProperty("#{TablaMaestraService}")
    private TablaMaestraService tablaMaestraService;


    @ManagedProperty("#{TablaMaestraEdicion}")
    private TablaMaestraEdicion tablaMaestraEdicion;

    @ManagedProperty("#{TablaMaestraProcessor}")
    private TablaMaestraProcessor tablaMaestraProcessor;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    public BusquedaTablaMaestraView() {
    }

    @PostConstruct
    public void init() {
        busqueda = "1";
        fechaFinalVisible=false;
        tablaMaestraRecords = tablaMaestraService.getCustomers();
        visibleRecords = false;
    }

    public void inicio(){

    }

    public void habilitarNuevo(){
        nuevaTabla = new TablaMaestraServicios();
        nuevaTabla.setTablaMeestra(selectedTabla);
        nuevaTabla.setEstado(true);
        nuevaTabla.setTipoDia(selectedTabla.getTipoDia());
    }

    public void actualizar(){

    }

    public void cancelar(){

    }

    public void nuevoRegistro(){
        boolean resultado = tablaMaestraProcessor.crearServicioTablaMaestraDefinitiva(nuevaTabla);
        if(resultado){
            messagesView.info(Messages.MENSAJE_EXITOSO,"");
            tServiciosRecords = tablaMaestraService.getServiciosByTabla(selectedTabla);
        }else{
            messagesView.error(Messages.MENSAJE_FALLO,"");
        }

    }

    public void eliminar(){
        for(TablaMaestraServicios servicios: selectedServiciosRecords){
            tablaMaestraService.deleteTServicios(servicios);
        }
        busquedaTablaMaestra();
    }

    public void reinciar(){
        busqueda = "1";
        fechaFinalVisible=false;
        tablaMaestraRecords = new ArrayList<>();
        visibleRecords = false;
        fechaFinal = null;
        fechaInicial = null;
        textoGeneracionMatrix="";
    }

    public void buscar(){
        visibleRecords=true;
        if (busqueda.equals("1")){
            if(fechaInicial!=null && tipoFecha!= null){
                tablaMaestraRecords = tablaMaestraService.getTablaMaestraByFecha(tipoFecha,fechaInicial);
            }else { messagesView.error(Messages.MENSAJE_CAMPOS_INCOMPLETOS,Messages.ACCION_CAMPOS_INCOMPLETOS);}
        }else if (busqueda.equals("2")){
            if( fechaInicial!= null && fechaFinal!=null && tipoFecha!= null){
                tablaMaestraRecords = tablaMaestraService.getTablaMaestraBetwenFechas(tipoFecha, fechaInicial,fechaFinal);
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
                    + "/secured/BuscarTablaMaestra.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void busquedaTablaMaestra(){

        List<TablaMaestraServicios> auxiliar =new ArrayList<>();
        visibleRecords=true;
        tServiciosRecords = tablaMaestraService.getServiciosByTabla(selectedTabla);

        for(TablaMaestraServicios tServicio:tServiciosRecords){
            tServicio.setTipoIntervalo(tipoIntervalo);
            auxiliar.add(tServicio);
        }

        if(tipoCiclo.equals("Optimo")){
            visibleOptimos=true;
            visibleMaximos=false;
            visibleMinimos=false;
        }else if(tipoCiclo.equals("Minimo")){
            visibleOptimos=false;
            visibleMaximos=false;
            visibleMinimos=true;
        }else{
            visibleOptimos=false;
            visibleMaximos=true;
            visibleMinimos=false;
        }

        tServiciosRecords=auxiliar;
        textoGeneracionMatrix="Tabla Maestra "+obtenerTipoTabla()+" generada para tipo ciclo: "+tipoCiclo+" y tipo intervalo: "+tipoIntervalo;
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            if(obtenerTipoTabla().equals("Definitiva")){
                ec.redirect(ec.getRequestContextPath()
                        + "/secured/tablaMaestra.xhtml");
            }else{
                ec.redirect(ec.getRequestContextPath()
                        + "/secured/TablaMaestraTemporal.xhtml");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String obtenerTipoTabla() {

        if(selectedTabla.getEsDefinitiva()){
            return "Definitiva";
        }
        return "Temporal";
    }

    public void onDateSelect(SelectEvent event) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Date Selected", format.format(event.getObject())));
    }



    public void edicionTablaTemporal(){
        for(TablaMaestraServicios servicioSeleccionado: selectedServiciosRecords){

            modificacionTipologia(servicioSeleccionado);
            tablaMaestraService.updateTServicios(servicioSeleccionado);
            tablaMaestraService.updateHorariosServicios(servicioSeleccionado.getHorariosServicio());
            tablaMaestraService.updateCicloServicio(servicioSeleccionado.getCicloServicio());
            tablaMaestraService.updateVelocidadProgramada(servicioSeleccionado.getVelocidadProgramada());



        }
    }

    public void onRowEdit(RowEditEvent event) {
        if(obtenerTipoTabla().equals("Definitiva")){
            for(TablaMaestraServicios servicioSeleccionado: selectedServiciosRecords){
                //Verificar que Id no exista
                if(!existeIdNombreOTrayecto(servicioSeleccionado)){
                    if(!existeTrayectoYNombreDeServicio(servicioSeleccionado)){
                        //Cambios por identificador
                        cambiosPorIdentificador( servicioSeleccionado );
                        //Cambios por trayecto

                        //Cambios por tipología
                        modificacionTipologia(servicioSeleccionado);

                        tablaMaestraService.updateTServicios(servicioSeleccionado);
                        tablaMaestraService.updateHorariosServicios(servicioSeleccionado.getHorariosServicio());

                        //Cambios por horarios
                    }else{
                        tServiciosRecords=tablaMaestraService.getServiciosByTabla(selectedTabla);
                        messagesView.error(Messages.MENSAJE_CAMPOS_REPETIDOS,Messages.MENSAJE_CALCULO_REVISION);
                    }

                }else{
                    tServiciosRecords=tablaMaestraService.getServiciosByTabla(selectedTabla);
                    messagesView.error(Messages.MENSAJE_CAMPOS_REPETIDOS,Messages.MENSAJE_CALCULO_REVISION);
                }
            }
        }else{
            edicionTablaTemporal();
        }

    }

    private void cambiosPorIdentificador(TablaMaestraServicios servicioSeleccionado) {
        String identificador = servicioSeleccionado.getIdentificador();
        Servicio nuevoServicio = tablaMaestraService.getServicioByIdentificador(identificador);
        if(nuevoServicio!=null){
               // servicioSeleccionado.setTrayecto(nuevoServicio.getTrayecto()+"");
                servicioSeleccionado.setMacro(nuevoServicio.getMacro());
                servicioSeleccionado.setLinea(nuevoServicio.getLinea());
                servicioSeleccionado.setSeccion(nuevoServicio.getSeccion());
                servicioSeleccionado.setNombreGeneral(nuevoServicio.getNombreGeneral());
               // servicioSeleccionado.setNombreEspecial(nuevoServicio.getNombreEspecial());
                servicioSeleccionado.setCodigoInicio(nuevoServicio.getPunto());

          GisServicio  gisServicio=tablaMaestraEdicion.getGisServicioByTrayectoLinea(identificador);
          if(gisServicio!=null){
              List<ArcoTiempo> arcoTiempoRecords = tablaMaestraEdicion.getArcoTiempoByGisCargaAndServicio(servicioSeleccionado.getTablaMeestra().getGisCarga(),gisServicio);
              if(arcoTiempoRecords.size()>0){
                  ArcoTiempo arcoTiempoBase = arcoTiempoRecords.get(0);
                  Nodo nodoInicio = tablaMaestraProcessor.getNodoInicio(arcoTiempoBase.getServicio().getNodoIncial());
                  if(nodoInicio!=null){
                      servicioSeleccionado =tablaMaestraProcessor.agregarInfoNodo(nuevoServicio,servicioSeleccionado,nodoInicio);
                      Nodo nodoFinal = tablaMaestraProcessor.getNodoInicio(arcoTiempoBase.getServicio().getNodoFinal());
                      if(nodoFinal!=null){
                          servicioSeleccionado =tablaMaestraProcessor.agregarInfoNodoFin(nuevoServicio,servicioSeleccionado,nodoFinal);
                          servicioSeleccionado.setTipoDia(arcoTiempoBase.getTipoDiaByArco().getTipoDia().getNombre());
                          servicioSeleccionado.setSecuencia(arcoTiempoBase.getSecuencia());
                          servicioSeleccionado= tablaMaestraProcessor.calcularDistancia(servicioSeleccionado,nodoInicio,nodoFinal,servicioSeleccionado.getTablaMeestra().getMatrizDistancia());

                           tablaMaestraProcessor.actualizarCicloServicio(servicioSeleccionado,arcoTiempoRecords);
                           tablaMaestraProcessor.actualizarVelocidadProgramada(servicioSeleccionado, servicioSeleccionado.getCicloServicio(),servicioSeleccionado.getDistancia());

                          tablaMaestraProcessor.actualizarHorarioServicios(nuevoServicio,servicioSeleccionado);


                      }

                  }
              }
          }

        }else{
            tServiciosRecords=tablaMaestraService.getServiciosByTabla(selectedTabla);
            messagesView.error(Messages.MENSAJE_FALLO,Messages.ACCION_REVISAR_ID);
        }
    }

    private boolean existeTrayectoYNombreDeServicio(TablaMaestraServicios servicioSeleccionado) {
        int repeticiones=0;
        for( TablaMaestraServicios servicios: tServiciosRecords){
            if(servicioSeleccionado.getTrayecto().equals(servicios.getTrayecto()) &&
                    servicioSeleccionado.getNombreEspecial().equals(servicios.getNombreEspecial())){
                repeticiones++;
            }
        }
        if(repeticiones>1){
            return true;
        }
        return false;
    }

    private TablaMaestraServicios modificacionTipologia(TablaMaestraServicios servicioSeleccionado) {
        String nuevoNombre = servicioSeleccionado.getTipologia().getNombre();
        Tipologia tipologia = tablaMaestraEdicion.obtenerTipologia(nuevoNombre);
        if(tipologia!=null){
            servicioSeleccionado.setTipologia(tipologia);

        }
        return servicioSeleccionado;
    }

    private boolean existeIdNombreOTrayecto(TablaMaestraServicios servicioSeleccionado) {
        int repeticiones=0;
        for( TablaMaestraServicios servicios: tServiciosRecords){
            if(servicioSeleccionado.getIdentificador().equals(servicios.getIdentificador())){
                       repeticiones++;
                    }
                }
        if(repeticiones>1){
            return true;
        }
        return false;

    }

    public void onRowCancel(RowEditEvent event) {
        System.out.println("sa");
    }

    public String getBusqueda() {
        return busqueda;
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

    public boolean isVisibleRecords() {
        return visibleRecords;
    }

    public void setVisibleRecords(boolean visibleRecords) {
        this.visibleRecords = visibleRecords;
    }

    public boolean isFechaFinalVisible() {
        return fechaFinalVisible;
    }

    public void setFechaFinalVisible(boolean fechaFinalVisible) {
        this.fechaFinalVisible = fechaFinalVisible;
    }

    public List<TablaMaestra> getTablaMaestraRecords() {
        return tablaMaestraRecords;
    }

    public void setTablaMaestraRecords(List<TablaMaestra> tablaMaestraRecords) {
        this.tablaMaestraRecords = tablaMaestraRecords;
    }

    public TablaMaestraServicios getServiciosSelected() {
        return serviciosSelected;
    }

    public void setServiciosSelected(TablaMaestraServicios serviciosSelected) {
        this.serviciosSelected = serviciosSelected;
    }

    public TablaMaestra getSelectedTabla() {
        return selectedTabla;
    }

    public void setSelectedTabla(TablaMaestra selectedTabla) {
        this.selectedTabla = selectedTabla;
    }

    public List<TablaMaestra> getFilteredTablaMaestra() {
        return filteredTablaMaestra;
    }

    public void setFilteredTablaMaestra(List<TablaMaestra> filteredTablaMaestra) {
        this.filteredTablaMaestra = filteredTablaMaestra;
    }

    public List<TablaMaestraServicios> gettServiciosRecords() {
        return tServiciosRecords;
    }

    public void settServiciosRecords(List<TablaMaestraServicios> tServiciosRecords) {
        this.tServiciosRecords = tServiciosRecords;
    }

    public List<TablaMaestraServicios> getFilteredTServiciosRecords() {
        return filteredTServiciosRecords;
    }

    public void setFilteredTServiciosRecords(List<TablaMaestraServicios> filteredTServiciosRecords) {
        this.filteredTServiciosRecords = filteredTServiciosRecords;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public TablaMaestraService getTablaMaestraService() {
        return tablaMaestraService;
    }

    public void setTablaMaestraService(TablaMaestraService tablaMaestraService) {
        this.tablaMaestraService = tablaMaestraService;
    }

    public TablaMaestraServicios getNuevaTabla() {
        return nuevaTabla;
    }

    public void setNuevaTabla(TablaMaestraServicios nuevaTabla) {
        this.nuevaTabla = nuevaTabla;
    }

    public String getTipoCiclo() {
        return tipoCiclo;
    }

    public void setTipoCiclo(String tipoCiclo) {
        this.tipoCiclo = tipoCiclo;
    }

    public String getTipoIntervalo() {
        return tipoIntervalo;
    }

    public void setTipoIntervalo(String tipoIntervalo) {
        this.tipoIntervalo = tipoIntervalo;
    }

    public boolean isVisibleOptimos() {
        return visibleOptimos;
    }

    public void setVisibleOptimos(boolean visibleOptimos) {
        this.visibleOptimos = visibleOptimos;
    }

    public boolean isVisibleMinimos() {
        return visibleMinimos;
    }

    public void setVisibleMinimos(boolean visibleMinimos) {
        this.visibleMinimos = visibleMinimos;
    }

    public boolean isVisibleMaximos() {
        return visibleMaximos;
    }

    public void setVisibleMaximos(boolean visibleMaximos) {
        this.visibleMaximos = visibleMaximos;
    }

    public String getTextoGeneracionMatrix() {
        return textoGeneracionMatrix;
    }

    public void setTextoGeneracionMatrix(String textoGeneracionMatrix) {
        this.textoGeneracionMatrix = textoGeneracionMatrix;
    }

    public List<TablaMaestraServicios> getSelectedServiciosRecords() {
        return selectedServiciosRecords;
    }

    public void setSelectedServiciosRecords(List<TablaMaestraServicios> selectedServiciosRecords) {
        this.selectedServiciosRecords = selectedServiciosRecords;
    }

    public TablaMaestraEdicion getTablaMaestraEdicion() {
        return tablaMaestraEdicion;
    }

    public void setTablaMaestraEdicion(TablaMaestraEdicion tablaMaestraEdicion) {
        this.tablaMaestraEdicion = tablaMaestraEdicion;
    }

    public TablaMaestraProcessor getTablaMaestraProcessor() {
        return tablaMaestraProcessor;
    }

    public void setTablaMaestraProcessor(TablaMaestraProcessor tablaMaestraProcessor) {
        this.tablaMaestraProcessor = tablaMaestraProcessor;
    }
}
