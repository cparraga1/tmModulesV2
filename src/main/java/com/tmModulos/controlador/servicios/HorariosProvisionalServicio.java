package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Service("HorariosProvisionalServicio")
@Transactional(readOnly = true)
public class HorariosProvisionalServicio {


    @Autowired
    TemporalHorariosDao temporalHorariosDao;

    @Autowired
    IntervalosProgramacionDao intervalosProgramacionDao;

    @Autowired
    TiempoIntervalosDao tiempoIntervalosDao;

    @Autowired
    TipoFranjaDao tipoFranjaDao;

    @Autowired
    IntervalosIdentificadorDao intervalosIdentificadorDao;

    @Autowired
    IntervalosDao intervalosDao;

    @Autowired
    GisIntervalosDao gisIntervalosDao;

    @Autowired
    ServicioTipoDiaDao servicioTipoDiaDao;



    public IntervalosProgramacion getIntervaloForDate(Time date){
        return intervalosProgramacionDao.getIntervaloForDate(date);
    }

    public List<IntervalosProgramacion> getAllIntervalos(){
        return intervalosProgramacionDao.getAllIntervalos();
    }

    public List<IntervalosProgramacion> getIntervaloByFranja(TipoFranja tipoFranja){
        return intervalosProgramacionDao.getIntervaloByFranja(tipoFranja);
    }

    @Transactional(readOnly = false)
    public void addTemporalHorarios(TemporalHorarios temporalHorarios) {
        temporalHorariosDao.addTemporalHorarios(temporalHorarios);
    }

    @Transactional(readOnly = false)
    public void deleteTemporalbyCuadro(String cuadro) {
        temporalHorariosDao.deleteTemporalbyCuadro(cuadro);
    }

    @Transactional(readOnly = false)
    public void addTiempoIntervalos(TiempoIntervalos tiempoIntervalos) {
        tiempoIntervalosDao.addTiempoIntervalos(tiempoIntervalos);
    }

    public TipoFranja getTipoFranjaByNombre(String nombre){
        return tipoFranjaDao.getTipoFranjaByNombre(nombre);
    }

    @Transactional(readOnly = false)
    public void addIntervalosId(IntervalosIdentificador intervalosIdentificador) {
        intervalosIdentificadorDao.addIntervalosId(intervalosIdentificador);
    }


    public IntervalosIdentificador getIntervalosIdentificadorByServicio(String id,String cuadro){
        return intervalosIdentificadorDao.getIntervalosIdentificadorByServicio(id,cuadro);
    }

    public List<IntervalosIdentificador> getAllIntervalosIdByCuadro(String cuadro){
        return intervalosIdentificadorDao.getAllIntervalosIdByCuadro(cuadro);
    }

    @Transactional(readOnly = false)
    public void addIntervalos(Intervalos intervalos) {
        intervalosDao.addIntervalos(intervalos);
    }


    public List<TiempoIntervalos> getTiempoIntervalosByServicio(List<IntervalosProgramacion> intervalos, ServicioTipoDia id,GisIntervalos gisIntervalos){
        return tiempoIntervalosDao.getTiempoIntervalosByServicio(intervalos,id,gisIntervalos);
    }

    @Transactional(readOnly = false)
    public void addGisIntervalo(GisIntervalos gisIntervalos) {
        gisIntervalosDao.addGisIntervalo(gisIntervalos);

    }

    public List<GisIntervalos> getGisIntervaloByFecha(String tipoFecha, Date fecha){
        return gisIntervalosDao.getGisIntervaloByFecha(tipoFecha,fecha);
    }



    public List<GisIntervalos> getGisIntervaloBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        return gisIntervalosDao.getGisIntervaloBetwenFechas(tipoFecha,fechaIni,fechaFin);
    }

    public List<GisIntervalos> getGisIntervalosAll() {
        return gisIntervalosDao.getGisIntervalosAll();
    }

    public List<Intervalos> getIntervalosByGisIntervalos(GisIntervalos gisIntervalos){
        return intervalosDao.getIntervalosByGisIntervalos(gisIntervalos);
    }

    public List<ServicioTipoDia> getServiciosByTipoDia(TipoDia tipoDia){
        return servicioTipoDiaDao.getServiciosByTipoDia(tipoDia);
    }

    }
