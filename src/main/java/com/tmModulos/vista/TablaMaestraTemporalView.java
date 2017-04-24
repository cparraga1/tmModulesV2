package com.tmModulos.vista;

import com.tmModulos.controlador.procesador.TablaMaestraEdicion;
import com.tmModulos.controlador.servicios.TablaMaestraService;
import com.tmModulos.modelo.entity.tmData.IntervalosServicio;
import com.tmModulos.modelo.entity.tmData.TablaMaestraServicios;
import com.tmModulos.modelo.entity.tmData.Tipologia;
import org.primefaces.event.RowEditEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name="temporal")
@SessionScoped
public class TablaMaestraTemporalView {

    @ManagedProperty("#{TablaMaestraService}")
    private TablaMaestraService tablaMaestraService;

    @ManagedProperty("#{TablaMaestraEdicion}")
    private TablaMaestraEdicion tablaMaestraEdicion;

    private List<TablaMaestraServicios> selectedServiciosRecords;

    public TablaMaestraTemporalView() {
    }

    public void edicionTablaTemporal(RowEditEvent event){
        for(TablaMaestraServicios servicioSeleccionado: selectedServiciosRecords){

                modificacionTipologia(servicioSeleccionado);
                tablaMaestraService.updateTServicios(servicioSeleccionado);
                tablaMaestraService.updateHorariosServicios(servicioSeleccionado.getHorariosServicio());
                tablaMaestraService.updateCicloServicio(servicioSeleccionado.getCicloServicio());
                tablaMaestraService.updateVelocidadProgramada(servicioSeleccionado.getVelocidadProgramada());
                List<IntervalosServicio> intervalosServiciossaRecords = servicioSeleccionado.getIntervalosServiciossaRecords();
                for(IntervalosServicio intervalosServicio: intervalosServiciossaRecords){
                    tablaMaestraService.updateIntervalosServicio(intervalosServicio);
                }

        }
    }

    private TablaMaestraServicios modificacionTipologia(TablaMaestraServicios servicioSeleccionado) {
        String nuevoNombre = servicioSeleccionado.getTipologia().getNombre();
        Tipologia tipologia = tablaMaestraEdicion.obtenerTipologia(nuevoNombre);
        if(tipologia!=null){
            servicioSeleccionado.setTipologia(tipologia);

        }
        return servicioSeleccionado;
    }

    public TablaMaestraService getTablaMaestraService() {
        return tablaMaestraService;
    }

    public void setTablaMaestraService(TablaMaestraService tablaMaestraService) {
        this.tablaMaestraService = tablaMaestraService;
    }

    public TablaMaestraEdicion getTablaMaestraEdicion() {
        return tablaMaestraEdicion;
    }

    public void setTablaMaestraEdicion(TablaMaestraEdicion tablaMaestraEdicion) {
        this.tablaMaestraEdicion = tablaMaestraEdicion;
    }

    public List<TablaMaestraServicios> getSelectedServiciosRecords() {
        return selectedServiciosRecords;
    }

    public void setSelectedServiciosRecords(List<TablaMaestraServicios> selectedServiciosRecords) {
        this.selectedServiciosRecords = selectedServiciosRecords;
    }
}
