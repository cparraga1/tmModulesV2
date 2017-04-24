package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ServicioService")
@Transactional(readOnly = true)
public class ServicioService {

    @Autowired
    TipologiaDao tipologiaDao;

    @Autowired
    FranjaHorariaDao franjaHorariaDao;

    @Autowired
    ServicioDao servicioDao;

    @Autowired
    ServicioTipoDiaDao servicioTipoDiaDao;

    @Autowired
    ServicioFranjaHorarioDao servicioFranjaHorarioDao;

    @Autowired
    TipoDiaDao tipoDiaDao;


    @Transactional(readOnly = false)
    public void addTipologia(Tipologia tipologia) {
        tipologiaDao.addTipologia(tipologia);
    }

    public void deleteTipologia(Tipologia tipologia) {
       tipologiaDao.deleteTipologia(tipologia);
    }

    @Transactional(readOnly = false)
    public void updateTipologia(Tipologia tipologia) {
        tipologiaDao.updateTipologia(tipologia);
    }


    public List<Tipologia> getTipologiaAll() {
     return tipologiaDao.getTipologiaAll();
    }

    public Tipologia getTipologiaByNombre(String nombre){
       return tipologiaDao.getTipologiaByNombre(nombre);
    }

    @Transactional(readOnly = false)
    public void addFranjaHoraria(FranjaHoraria franjaHoraria) {
        franjaHorariaDao.addFranjaHoraria(franjaHoraria);
    }

    public void deleteFranjaHoraria(FranjaHoraria franjaHoraria) {
        franjaHorariaDao.deleteFranjaHoraria(franjaHoraria);
    }

    @Transactional(readOnly = false)
    public void updateFranjaHoraria(FranjaHoraria franjaHoraria) {
       franjaHorariaDao.updateFranjaHoraria(franjaHoraria);
    }


    public List<FranjaHoraria> getFranjaHorariaAll() {
      return franjaHorariaDao.getFranjaHorariaAll();
    }

    public TipologiaDao getTipologiaDao() {
        return tipologiaDao;
    }

    public void setTipologiaDao(TipologiaDao tipologiaDao) {
        this.tipologiaDao = tipologiaDao;
    }

    public FranjaHorariaDao getFranjaHorariaDao() {
        return franjaHorariaDao;
    }

    public void setFranjaHorariaDao(FranjaHorariaDao franjaHorariaDao) {
        this.franjaHorariaDao = franjaHorariaDao;
    }

    public ServicioDao getServicioDao() {
        return servicioDao;
    }

    public void setServicioDao(ServicioDao servicioDao) {
        this.servicioDao = servicioDao;
    }

    public ServicioFranjaHorarioDao getServicioFranjaHorarioDao() {
        return servicioFranjaHorarioDao;
    }

    public void setServicioFranjaHorarioDao(ServicioFranjaHorarioDao servicioFranjaHorarioDao) {
        this.servicioFranjaHorarioDao = servicioFranjaHorarioDao;
    }
    @Transactional(readOnly = false)
    public void addServicioFranjaHorario(ServicioFranjaHorario servicioFranjaHorario) {
        servicioFranjaHorarioDao.addServicioFranjaHorario(servicioFranjaHorario);
    }

    public void deleteServicioFranjaHorario(ServicioFranjaHorario servicioFranjaHorario) {
    servicioFranjaHorarioDao.deleteServicioFranjaHorario(servicioFranjaHorario);
    }

    @Transactional(readOnly = false)
    public void updateServicioFranjaHorario(ServicioFranjaHorario servicioFranjaHorario) {
        servicioFranjaHorarioDao.updateServicioFranjaHorario(servicioFranjaHorario);
    }


    public List<ServicioFranjaHorario> getServicioFranjaHorarioAll() {
       return servicioFranjaHorarioDao.getServicioFranjaHorarioAll();
    }

    @Transactional(readOnly = false)
    public void addServicio(Servicio servicio) {
        servicioDao.addServicio( servicio );

    }

    public void deleteServicio(Servicio servicio) {
        servicioDao.deleteServicio( servicio );
    }

    @Transactional(readOnly = false)
    public void updateServicio(Servicio servicio) {
        servicioDao.updateServicio( servicio );
    }


    public List<Servicio> getServicioAll() {
        return servicioDao.getServicioAll();
    }

    public List<ServicioTipoDia> getServiciosById(String Identificador){
        return servicioTipoDiaDao.getServiciosById(Identificador);
    }

    public List<TipoDia> getTipoDiaAll() {
        return tipoDiaDao.getTipoDiaAll();
    }

    public TipoDia getTipoDia(String nombre){
        return tipoDiaDao.getTipoDia(nombre);
    }


    public List<ServicioTipoDia> getServiciosByTipoDia(TipoDia tipoDia){
        return servicioTipoDiaDao.getServiciosByTipoDia(tipoDia);
    }
    public List<ServicioTipoDia> getTServiciosAll() {
        return servicioTipoDiaDao.getTServiciosAll();
    }

    public Servicio getServicioByNombreEspecial(String nombre){
        return servicioDao.getServicioByNombreEspecial(nombre);
    }

    @Transactional(readOnly = false)
    public void addServicio(ServicioTipoDia servicio) {
        servicioTipoDiaDao.addServicio(servicio);
    }

    @Transactional(readOnly = false)
    public void deleteServicioTipoDia(ServicioTipoDia servicio) {
        servicioTipoDiaDao.deleteServicioTipoDia(servicio);
    }

    @Transactional(readOnly = false)
    public void updateServicioTipoDia(ServicioTipoDia servicio) {
        servicioTipoDiaDao.updateServicioTipoDia(servicio);
    }

    @Transactional(readOnly = false)
    public void updateListaServicioTipoDia(List<ServicioTipoDia> servicios) {
        servicioTipoDiaDao.updateListaServicioTipoDia(servicios);
    }

    public List<ServicioTipoDia> getServiciosByServicio(Servicio servicio){
        return servicioTipoDiaDao.getServiciosByServicio(servicio);
    }

}
