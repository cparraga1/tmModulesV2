package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.EquivalenciasDao;
import com.tmModulos.modelo.dao.tmData.ExpedicionesTemporalDao;
import com.tmModulos.modelo.dao.tmData.TempHorarioDao;
import com.tmModulos.modelo.dao.tmData.TempPosDao;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Time;
import java.util.List;

@Service("VeriPreHorarios")
public class VeriPreHorarios {


    @Autowired
    public ExpedicionesTemporalDao expedicionesTemporalDao;

    @Autowired
    public EquivalenciasDao equivalenciasDao;

    @Autowired
    public TempPosDao tempHorarioDao;


    public void addExpTemporal(ExpedicionesTemporal temporal) {
        expedicionesTemporalDao.addExpTemporal(temporal);
    }

    public void deleteExpTemporal(ExpedicionesTemporal temporal) {
        expedicionesTemporalDao.deleteExpTemporal(temporal);
    }


    public void updateExpTemporal(ExpedicionesTemporal temporal) {
       expedicionesTemporalDao.updateExpTemporal(temporal);
    }


    public List<ExpedicionesTemporal> getExpTemporalAll() {
        return expedicionesTemporalDao.getExpTemporalAll();
    }

    public List<ExpedicionesTemporal> getExpedicionesTemporalsData(String id){
        return expedicionesTemporalDao.getExpedicionesTemporalsData(id);
    }


    public List<Equivalencias> getEquivalenciasByData(int linea, int sublinea, int puntoI, int puntoF){
     return equivalenciasDao.getEquivalenciasByData(linea,sublinea,puntoI,puntoF);
    }

    public void addEquivalenciasFromFile(String filename){
        equivalenciasDao.addEquivalenciasFromFile(filename);
    }

    public void deleteEquivalencias(){
        equivalenciasDao.deleteEquivalencias();
    }

    public void addTablaHorarioFromFile(String filename){
        tempHorarioDao.addTablaHorarioFromFile(filename);
    }

    public void addTablaHorarioTMFromFile(String filename){
        tempHorarioDao.addTablaHorarioTMFromFile(filename);
    }

    public void deleteTablaHorario(){
        tempHorarioDao.deleteTablaHorario();
    }

    public List<TempPos> getTablaHorarioByData(int linea, int sublinea, int ruta, int punto){
        return tempHorarioDao.getTablaHorarioByData(linea,sublinea,ruta,punto);
    }

    public Time getSumInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        return tempHorarioDao.getSumInstanteByFranjaHora(idServicio,inicio,fin);
    }


}


