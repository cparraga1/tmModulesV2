package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.IntervalosProgramacion;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TiempoIntervalos;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class IntervalosConversionServicio {

    @Autowired
    GisIntervalosDao gisIntervalosDao;

    @Autowired
    IntervalosProgramacionDao intervalosProgramacionDao;

    @Autowired
    ServicioTipoDiaDao servicioTipoDiaDao;

    @Autowired
    TiempoIntervalosDao tiempoIntervalosDao;

    @Autowired
    TipoDiaDao tipoDiaDao;

    public List<IntervalosProgramacion> getAllIntervalos(){
        return intervalosProgramacionDao.getAllIntervalos();
    }

    public List<ServicioTipoDia> getServiciosByTipoDia(TipoDia tipoDia){
        return servicioTipoDiaDao.getServiciosByTipoDia(tipoDia);
    }

    public TipoDia getTipoDia(String nombre){
        return tipoDiaDao.getTipoDia(nombre);
    }

    public List<TiempoIntervalos> getTiempoIntervalosByHoraServicioCuadro
            (IntervalosProgramacion intervalo, ServicioTipoDia tipoDia,String cuadro){
        return tiempoIntervalosDao.getTiempoIntervalosByHoraServicioCuadro(intervalo,tipoDia,cuadro);
    }

}
