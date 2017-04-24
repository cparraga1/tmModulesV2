package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GisCargaService {

    @Autowired
    GisCargaDao gisCargaDao;



    @Autowired
    ArcoTiempoDao arcoTiempoDao;

    @Autowired
    ServicioDao servicioDao;

    @Autowired
    TipologiaDao tipologiaDao;

    @Autowired
    GisServicioDao gisServicioDao;




    @Transactional(readOnly = false)
    public void addGisCarga(GisCarga gisCarga) {
        gisCargaDao.addGisCarga(gisCarga);
    }

    public void deleteGisCarga(GisCarga gisCarga) {
        gisCargaDao.deleteGisCarga(gisCarga);
    }

    public void updateGisCarga(GisCarga gisCarga) {
        gisCargaDao.updateGisCarga(gisCarga);
    }

    public List<GisCarga> getGisCargaAll() {
        return gisCargaDao.getGisCargaAll();
    }
    public List<GisCarga> getGisCargaByFecha(String tipoFecha,Date fecha){ return  gisCargaDao.getGisCargaByFecha( tipoFecha, fecha);}
    public List<GisCarga> getGisCargaBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){ return  gisCargaDao.getGisCargaBetwenFechas(tipoFecha,fechaIni,fechaFin);}


    @Transactional(readOnly = false)
    public void addArcoTiempo(ArcoTiempo arcoTiempo) { arcoTiempoDao.addArcoTiempo( arcoTiempo );}

    public void deleteArcoTiempo(ArcoTiempo arcoTiempo) { arcoTiempoDao.deleteArcoTiempo( arcoTiempo );}


    public void updateArcoTiempo(ArcoTiempo arcoTiempo) { arcoTiempoDao.updateArcoTiempo( arcoTiempo );}


    public List<ArcoTiempo> getArcoTiempoAll() { return  arcoTiempoDao.getArcoTiempoAll(); }

    public List<ArcoTiempo> getArcoTiempoByGisCarga(GisCarga gisCarga){ return arcoTiempoDao.getArcoTiempoByGisCarga(gisCarga);}

    public Servicio getServicioByTrayecto(int trayecto,int punto){
        return servicioDao.getServicioByTrayecto(trayecto, punto);
    }
    public List<Servicio> getServicioAll() {
        return servicioDao.getServicioAll();
    }

    @Transactional(readOnly = false)
    public void addServicio(Servicio servicio) {
        servicioDao.addServicio(servicio);

    }

    public List<Tipologia> getTipologiaAll() {
        return tipologiaDao.getTipologiaAll();
    }

    public List<ArcoTiempo> getArcoTiempoById(long id){
        return arcoTiempoDao.getArcoTiempoById(id);
    }

    public GisCarga getGisCargaById(String id){
      return  gisCargaDao.getGisCargaById(id);
    }

    public List<ArcoTiempo> getArcoTiempoByGisCargaAndServicio(GisCarga gisCarga,GisServicio servicio){
        return arcoTiempoDao.getArcoTiempoByGisCargaAndServicio(gisCarga,servicio);
    }

    @Transactional(readOnly = false)
    public void addGisServicio(GisServicio gisServicio)
    {
        gisServicioDao.addGisServicio(gisServicio);
    }
    @Transactional(readOnly = false)
    public void deleteGisServicio(GisServicio gisServicio) {
       gisServicioDao.deleteGisServicio(gisServicio);
    }

    @Transactional(readOnly = false)
    public void updateGisServicio(GisServicio gisServicio) {
        gisServicioDao.updateGisServicio(gisServicio);
    }


    public List<GisServicio> getGisServicioAll() {
      return gisServicioDao.getGisServicioAll();
    }

    public GisServicio getGisServicioByTrayectoLinea(String identificador){
        return gisServicioDao.getGisServicioByTrayectoLinea(identificador);
    }


}
