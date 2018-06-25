package com.tmModulos.controlador.servicios;


import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.Horario;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import com.tmModulos.modelo.entity.tmData.VerificacionTipoDia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service("ConfService")
@Transactional(readOnly = true)
public class ConfVeriHorario {

    @Autowired
    VerificacionTipoDiaDao verificacionTipoDiaDao;

    @Autowired
    TipoDiaDao tipoDiaDao;

    @Autowired
    HorarioDao horarioDao;

    @Autowired
    ServicioTipoDiaDao servicioTipoDiaDao;


    public ConfVeriHorario() {
    }

    @Transactional(readOnly = false)
    public void addVerificacionTipoDia(TipoDia tipoDia) {
       tipoDiaDao.addTipoDia(tipoDia);

    }

    @Transactional(readOnly = false)
    public void deleteVerificacionTipoDia(TipoDia tipoDia) {
        tipoDiaDao.deleteTipoDia(tipoDia);
    }

    @Transactional(readOnly = false)
    public void updateVerificacionTipoDia(TipoDia tipoDia) {
        tipoDiaDao.updateTipoDia(tipoDia);
    }


    public List<TipoDia> getTipoDiaAll() {
       return tipoDiaDao.getTipoDiaAll();
    }

    public TipoDia getTipoDia(String tipoDia) {
        return tipoDiaDao.getTipoDia(tipoDia);
    }

    @Transactional(readOnly = false)
    public void duplicarTipoDia(TipoDia duplicadoTipoDia, TipoDia aDuplicar) {
        //Duplicar datos
        tipoDiaDao.addTipoDia(duplicadoTipoDia);

        // Cargar horarios asociados al tipo dia a duplicar
        List<Horario> horariosTipoDia = horarioDao.getHorarioByTipoDia(aDuplicar);

        //Cargar Servicios Tipo Dia
        List<ServicioTipoDia> serviciosByTipoDia = servicioTipoDiaDao.getServiciosByTipoDia(aDuplicar);


        //Duplicar Horarios
        for(Horario horario:horariosTipoDia){
            Horario nuevoHorario = new Horario();
            nuevoHorario.setConfig(horario.getConfig());
            nuevoHorario.setHoraFin(horario.getHoraFin());
            nuevoHorario.setHoraInicio(horario.getHoraInicio());
            nuevoHorario.setServicio(horario.getServicio());
            nuevoHorario.setTipoHorario(horario.getTipoHorario());
            nuevoHorario.setTipoDia(duplicadoTipoDia);
            horarioDao.addHorarios(nuevoHorario);
        }

        //Duplicar Servicio Tipo Dia
        for(ServicioTipoDia servicioTipoDia: serviciosByTipoDia){
            ServicioTipoDia nuevoServicioTipoDia = new ServicioTipoDia();
            nuevoServicioTipoDia.setIdentificador(servicioTipoDia.getIdentificador());
            nuevoServicioTipoDia.setOrden(servicioTipoDia.getOrden());
            nuevoServicioTipoDia.setServicio(servicioTipoDia.getServicio());
            nuevoServicioTipoDia.setTipoDia(duplicadoTipoDia);
            servicioTipoDiaDao.addServicio(nuevoServicioTipoDia);
        }


    }

    public List<Horario> getHorariosTipoDia(TipoDia selectedTipoDia) {
        return  horarioDao.getHorarioByTipoDia(selectedTipoDia);
    }
}
