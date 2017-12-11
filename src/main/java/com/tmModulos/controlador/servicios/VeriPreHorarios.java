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

    public List<String> getExpedicionesNoReferenciadas(List<String> serviciosEncontrados){
        return expedicionesTemporalDao.getExpedicionesNoReferenciadas(serviciosEncontrados);
    }


    public List<Equivalencias> getEquivalenciasByData(int linea, int sublinea, int puntoI, int puntoF){
     return equivalenciasDao.getEquivalenciasByData(linea,sublinea,puntoI,puntoF);
    }

    public void addEquivalenciasFromFile(String filename) throws Exception {
        try{
            equivalenciasDao.addEquivalenciasFromFile(filename);
        }catch (Exception e){
            throw new Exception("Archivo con Informacion no esperada");
        }

    }

    public void deleteEquivalencias(){
        equivalenciasDao.deleteEquivalencias();
    }

    public void addTablaHorarioFromFile(String filename) throws Exception {
        try{
            tempHorarioDao.addTablaHorarioFromFile(filename,";");
        }catch (Exception e){
           addTablaHorarioFormatoComa(filename);

        }
    }

    private void addTablaHorarioFormatoComa(String filename) throws Exception {
        try {
            tempHorarioDao.addTablaHorarioFromFile(filename,",");
        }catch (Exception e){
            throw new Exception("Archivo con Informacion no esperada");
        }
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

    public List<String> getTempPosNoReferenciadas(List<String> serviciosEncontrados){
        return tempHorarioDao.getTempPosNoReferenciadas(serviciosEncontrados);
    }

    public Time getSumInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        return tempHorarioDao.getSumInstanteByFranjaHora(idServicio,inicio,fin);
    }

    public String findDelimiterFromFile (String filename){
//        filename.split(".")
        return ";";
    }

}


