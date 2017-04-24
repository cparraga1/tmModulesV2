package com.tmModulos.controlador.servicios;


import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service("TablaMaestraService")
@Transactional(readOnly = true)
public class TablaMaestraService {


    @Autowired
    TablaMaestraDao tablaMaestraDao;

    @Autowired
    TablaMaestraServiciosDao tablaMaestraServiciosDao;

    @Autowired
    TipoFranjaDao tipoFranjaDao;

    @Autowired
    CicloServicioDao cicloServicioDao;

    @Autowired
    private IntervalosDao intervalosDao;

    @Autowired
    IntervalosServicioDao intervalosServicioDao;

    @Autowired
    VelocidadProgramadaDao velocidadProgramadaDao;

    @Autowired
    HorariosServicioDao horariosServicioDao;

    @Autowired
    HorarioDao horarioDao;

    @Autowired
    ServicioDao servicioDao;


    @Autowired
    TipologiaDao tipologiaDao;



    @Transactional(readOnly = false)
    public void addCustomer(TablaMaestra tablaMaestra) {
        getTablaMaestraDao().addCustomer(tablaMaestra);
    }

    @Transactional(readOnly = false)
    public void deleteCustomer(TablaMaestra customer) {
        getTablaMaestraDao().deleteCustomer(customer);
    }

    @Transactional(readOnly = false)
    public void updateCustomer(TablaMaestra customer) {
        getTablaMaestraDao().updateCustomer(customer);
    }

    public TablaMaestra getCustomerById(int id) {
        return getTablaMaestraDao().getCustomerById(id);
    }

    @Transactional(readOnly = false)
    public List<TablaMaestra> getCustomers() {
        return getTablaMaestraDao().getCustomers();
    }

    public TablaMaestraDao getTablaMaestraDao() {
        return tablaMaestraDao;
    }

    public void setTablaMaestraDao(TablaMaestraDao tablaMaestraDao) {
        this.tablaMaestraDao = tablaMaestraDao;
    }

    @Transactional(readOnly = false)
    public void addTServicios(TablaMaestraServicios tablaMaestraServicios) {
        tablaMaestraServiciosDao.addTServicios(tablaMaestraServicios);
    }

    @Transactional(readOnly = false)
    public void deleteTServicios(TablaMaestraServicios tablaMaestraServicios) {
        tablaMaestraServiciosDao.deleteTServicios(tablaMaestraServicios);
    }

    @Transactional(readOnly = false)
    public void addIntervalos(Intervalos intervalos) {
      intervalosDao.addIntervalos(intervalos);
    }

    @Transactional(readOnly = false)
    public void updateTServicios(TablaMaestraServicios tablaMaestraServicios) {
        tablaMaestraServiciosDao.updateTServicios(tablaMaestraServicios);
    }


    public List<TablaMaestraServicios> getTServicios() {
        return tablaMaestraServiciosDao.getTServicios();
    }


    public List<TablaMaestra> getTablaMaestraByFecha(String tipoFecha, Date fecha){
        return tablaMaestraDao.getTablaMaestraByFecha(tipoFecha,fecha);
    }

    public List<TablaMaestra> getTablaMaestraBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        return tablaMaestraDao.getTablaMaestraBetwenFechas(tipoFecha,fechaIni,fechaFin);
    }

    public TablaMaestra getUltimaTablaMaestraByaTipoDia(String tipoDia,Date fechaCreacion){
        return tablaMaestraDao.getUltimaTablaMaestraByaTipoDia(tipoDia,fechaCreacion);
    }

    public List<TablaMaestraServicios> getServiciosByTabla(TablaMaestra tablaMaestra){
        return tablaMaestraServiciosDao.getServiciosByTabla(tablaMaestra);
    }

    public List<TipoFranja> getTipoFranjaAll() {
        return tipoFranjaDao.getTipoFranjaAll();
    }

    public TipoFranja getTipoFranjaByHorario(String horaIncio,String horaFin){
        return tipoFranjaDao.getTipoFranjaByHorario(horaIncio,horaFin);
    }

    @Transactional(readOnly = false)
    public void addIntervalosServicio(IntervalosServicio intervalosServicio) {
       intervalosServicioDao.addIntervalosServicio(intervalosServicio);
    }

    @Transactional(readOnly = false)
    public void deleteIntervalosServicios(IntervalosServicio intervalosServicio) {
        intervalosServicioDao.deleteIntervalosServicios(intervalosServicio);
    }

    @Transactional(readOnly = false)
    public void updateIntervalosServicio(IntervalosServicio intervalosServicio) {
        intervalosServicioDao.updateIntervalosServicio(intervalosServicio);
    }


    public List<IntervalosServicio> getIntervalosServicioAll() {
       return intervalosServicioDao.getIntervalosServicioAll();
    }

    public List<IntervalosServicio> getIntervalosServicioByTabla(TablaMaestraServicios tablaMaestra){
        return intervalosServicioDao.getIntervalosServicioByTabla(tablaMaestra);
    }

    @Transactional(readOnly = false)
    public void addCicloServicio(CicloServicio cicloServicio) {
        cicloServicioDao.addCicloServicio(cicloServicio);
    }

    @Transactional(readOnly = false)
    public void deleteCicloServicio(CicloServicio cicloServicio) {
       cicloServicioDao.deleteCicloServicio(cicloServicio);
    }

    @Transactional(readOnly = false)
    public void updateCicloServicio(CicloServicio cicloServicio) {
        cicloServicioDao.updateCicloServicio(cicloServicio);
    }


    public List<CicloServicio> getCicloServiciosAll() {
       return cicloServicioDao.getCicloServiciosAll();
    }

    public List<CicloServicio> getCicloServicioByTabla(TablaMaestraServicios tablaMaestra){
        return getCicloServicioByTabla(tablaMaestra);
    }

    @Transactional(readOnly = false)
    public void addVelocidadProgramada(VelocidadProgramada velocidadProgramada) {
       velocidadProgramadaDao.addVelocidadProgramada(velocidadProgramada);
    }

    @Transactional(readOnly = false)
    public void deleteVelocidadProgramada(VelocidadProgramada velocidadProgramada) {
        velocidadProgramadaDao.deleteVelocidadProgramada(velocidadProgramada);
    }

    @Transactional(readOnly = false)
    public void updateVelocidadProgramada(VelocidadProgramada velocidadProgramada) {
        velocidadProgramadaDao.updateVelocidadProgramada(velocidadProgramada);
    }


    public List<VelocidadProgramada> getVelocidadProgramadaAll() {
       return velocidadProgramadaDao.getVelocidadProgramadaAll();
    }

    @Transactional(readOnly = false)
    public void addHorariosServicios(HorariosServicio horariosServicio) {
       horariosServicioDao.addHorariosServicios(horariosServicio);
    }

    @Transactional(readOnly = false)
    public void deleteHorariosServicios(HorariosServicio horariosServicio) {
        horariosServicioDao.deleteHorariosServicios(horariosServicio);
    }

    @Transactional(readOnly = false)
    public void updateHorariosServicios(HorariosServicio horariosServicio) {
       horariosServicioDao.updateHorariosServicios(horariosServicio);
    }


    public List<GisServicio> getHorariosServiciosAll() {
        return horariosServicioDao.getHorariosServiciosAll();

    }

    @Transactional(readOnly = false)
    public void addHorarios(Horario horariosServicio) {
        horarioDao.addHorarios(horariosServicio);
    }

    @Transactional(readOnly = false)
    public void deleteHorarios(Horario horariosServicio) {
        horarioDao.deleteHorarios(horariosServicio);
    }

    @Transactional(readOnly = false)
    public void updateHorarios(Horario horariosServicio) {
        horarioDao.updateHorarios(horariosServicio);
    }


    public List<Horario> getHorariosByServicio(Servicio servicio){
        return horarioDao.getHorariosByServicio(servicio);
    }

    public Servicio getServicioByIdentificador(String identificador){
        return servicioDao.getServicioByIdentificador(identificador);
    }

    public Tipologia getTipologiaByNombre(String nombre){
        return tipologiaDao.getTipologiaByNombre(nombre);
    }
}
