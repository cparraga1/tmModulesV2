package com.tmModulos.controlador.servicios;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


public class Util {

    public static HttpSession getSession(){
        return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest(){
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public static List<String> listaDePeriocidad(){
        List<String> periocidad = new ArrayList<>();
        periocidad.add("Habil");
        periocidad.add("Festivo");
        periocidad.add("Sabado");
        return periocidad;
    }

    public static List<String> listaDeTipologia(){
        List<String> tipologia = new ArrayList<>();
        tipologia.add("ART");
        tipologia.add("BI");
        tipologia.add("DEF");
        return tipologia;
    }

    public static List<String> listaDeTipoGrafica(){
        List<String> tipoGrafica = new ArrayList<>();
        tipoGrafica.add("Con Saltos");
        tipoGrafica.add("Con Repeticiones");
        return tipoGrafica;
    }
}
