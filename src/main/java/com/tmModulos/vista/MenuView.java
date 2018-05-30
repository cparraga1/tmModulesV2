package com.tmModulos.vista;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;

@ManagedBean(name="menu")
@SessionScoped
public class MenuView {


    public void refreshServicios(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ServiciosParametrizacion.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshExpedicionesConvertor(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ConversorExpediciones.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshFranjaHoraria () {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/FranjaHorarioParametrizacion.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshServiciosporDia(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ServiciosPorDiaParametrizacion.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshNodo(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/NodosParametrizacion.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshZonas(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ParametrizarZonas.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshNuevaTablaMaestra(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/NuevaTablaMaestra.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBuscarTablaMaestra(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/BuscarTablaMaestra.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshNuevoGISCarga(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/NewGISCarga.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBuscarGISCarga(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/BuscarGISCarga.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshNuevaMatrizDistancia(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/NuevaMatrizDistancias.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshVerificacionHorarios(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/VerifHorarios.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBusesHoraConvertor(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ConversionABusesHora.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshConfVerificacionHorarios(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/ParametrizacionVerificacionHorario.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void refreshBuscarMatrizDistancia(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/BuscarMatrizDistancia.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void refreshNuevosIntervalosGIS(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/NuevoIntervalosGIS.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshBuscarIntervalosGIS(){
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        try {
            ec.redirect(ec.getRequestContextPath()
                    + "/secured/BuscarGisIntervalos.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
