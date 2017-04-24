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
