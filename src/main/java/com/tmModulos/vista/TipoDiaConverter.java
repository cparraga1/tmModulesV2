package com.tmModulos.vista;

import com.tmModulos.modelo.dao.tmData.TipoDiaDao;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Service
public class TipoDiaConverter implements Converter {

    @Autowired
    TipoDiaDao tipoDiaDao;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String objectString) {
        if(objectString!=null){
            return tipoDiaDao.getTipoDia(objectString);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object tipodia) {
        return ((TipoDia)tipodia).getNombre();
    }

}
