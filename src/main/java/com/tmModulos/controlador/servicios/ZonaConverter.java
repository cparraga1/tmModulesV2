package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.NodoDao;
import com.tmModulos.modelo.dao.tmData.ZonaDao;
import com.tmModulos.modelo.entity.tmData.Nodo;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.springframework.beans.factory.annotation.Autowired;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean(name="zonaConverter")
public class ZonaConverter implements Converter {

    @ManagedProperty("#{NodoService}")
    private NodoService nodoService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return nodoService.getZonaByName(value,"P");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Zona) value).toString();
    }

    public NodoService getNodoService() {
        return nodoService;
    }

    public void setNodoService(NodoService nodoService) {
        this.nodoService = nodoService;
    }
}
