package com.tmModulos.controlador.procesador;

import com.tmModulos.controlador.servicios.GisCargaService;
import com.tmModulos.controlador.servicios.ServicioService;
import com.tmModulos.modelo.entity.tmData.ArcoTiempo;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.GisServicio;
import com.tmModulos.modelo.entity.tmData.Tipologia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TablaMaestraEdicion")
public class TablaMaestraEdicion {

    @Autowired
    public ServicioService servicioService;

    @Autowired
    GisCargaService gisCargaService;

    public Tipologia obtenerTipologia(String nombre){
        Tipologia tipologiaByNombre = servicioService.getTipologiaByNombre(nombre);
//        if(tipologiaByNombre==null){
//            tipologiaByNombre= servicioService.getTipologiaByNombre("ARTICULADO");
//        }
        return tipologiaByNombre;
    }

    public GisServicio getGisServicioByTrayectoLinea(String identificador){
        return gisCargaService.getGisServicioByTrayectoLinea(identificador);
    }

    public List<ArcoTiempo> getArcoTiempoByGisCargaAndServicio(GisCarga gisCarga, GisServicio servicio){
        return gisCargaService.getArcoTiempoByGisCargaAndServicio(gisCarga,servicio);
    }

}
