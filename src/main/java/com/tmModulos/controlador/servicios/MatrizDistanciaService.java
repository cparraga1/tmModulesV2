package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.*;
import com.tmModulos.modelo.entity.tmData.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("MatrizDistanciaService")
@Transactional(readOnly = true)
public class MatrizDistanciaService {

    @Autowired
    MatrizDistanciaDao matrizDistanciaDao;

    @Autowired
    ServicioDao servicioDao;

    @Autowired
    DistanciaNodosDao distanciaNodosDao;

    @Autowired
    ServicioDistanciaDao servicioDistanciaDao;

    @Autowired
    ListaNegraMatrizDao listaNegraMatrizDao;


    @Transactional(readOnly = false)
    public void addMatrizDistancia(MatrizDistancia matrizDistancia) {
        matrizDistanciaDao.addMatrizDistancia( matrizDistancia );

    }

    public void deleteMatrizDistancia(MatrizDistancia matrizDistancia) {
        matrizDistanciaDao.deleteMatrizDistancia(matrizDistancia);
    }

    @Transactional(readOnly = false)
    public void updateMatrizDistancia(MatrizDistancia matrizDistancia) {
        matrizDistanciaDao.updateMatrizDistancia(matrizDistancia);
    }


    public List<MatrizDistancia> getMatrizDistanciaAll() {
        return matrizDistanciaDao.getMatrizDistanciaAll();
    }

    public List<MatrizDistancia> getMatrizDistanciaByFecha(String tipoFecha,Date fecha){
        return matrizDistanciaDao.getMatrizDistanciaByFecha(tipoFecha,fecha);
    }

    public List<MatrizDistancia> getMatrizDistanciaBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        return matrizDistanciaDao.getMatrizDistanciaBetwenFechas(tipoFecha,fechaIni,fechaFin);
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

    @Transactional(readOnly = false)
    public void addDistanciaNodos(DistanciaNodos distanciaNodos) {
        distanciaNodosDao.addDistanciaNodos(distanciaNodos);

    }

    @Transactional(readOnly = false)
    public void deleteDistanciaNodos(DistanciaNodos distanciaNodos) {
        distanciaNodosDao.deleteDistanciaNodos(distanciaNodos);
    }

    @Transactional(readOnly = false)
    public void updateDistanciaNodos(DistanciaNodos distanciaNodos) {
        distanciaNodosDao.updateDistanciaNodos(distanciaNodos);
    }

    public List<DistanciaNodos> getDistanciaNodosAll() {
        return distanciaNodosDao.getDistanciaNodosAll();
    }

    public List<DistanciaNodos> getDistanciaNodosByMatriz(MatrizDistancia matrizDistancia){
        return distanciaNodosDao.getDistanciaNodosByMatriz( matrizDistancia );
    }

    public Servicio getServicioBymacroLineaYseccion(int macro,int linea,int seccion, int nodo){
        return servicioDao.getServicioBymacroLineaYseccion( macro,linea,seccion,nodo );
    }

    public MatrizDistancia getMatrizDistanciaById(String id){
       return matrizDistanciaDao.getMatrizDistanciaById(id);
    }

    @Transactional(readOnly = false)
    public void addServicioDistancia(ServicioDistancia servicioDistancia) {
        servicioDistanciaDao.addServicioDistancia(servicioDistancia);
    }

    public void deleteServicioDistancia(ServicioDistancia servicioDistancia) {
        servicioDistanciaDao.deleteServicioDistancia(servicioDistancia);
    }


    public void updateServicioDistancia(ServicioDistancia servicioDistancia) {
        servicioDistanciaDao.updateServicioDistancia(servicioDistancia);
    }


    public List<ServicioDistancia> getServicioDistanciaAll() {
        return servicioDistanciaDao.getServicioDistanciaAll();
    }

    public ServicioDistancia getServicioDistanciaByMacroLineaSeccion(int macro, int linea, int seccion){
        return servicioDistanciaDao.getServicioDistanciaByMacroLineaSeccion(macro,linea,seccion);
    }

    public ServicioDistancia getServicioDistanciaByIdentificador(String identificador){
        return servicioDistanciaDao.getServicioDistanciaByIdentificador(identificador);
    }
    public DistanciaNodos getDistanciaNodosByServicioAndPunto(ServicioDistancia servicioDistancia, MatrizDistancia matrizDistancia, String nodoCodigo){
       return distanciaNodosDao.getDistanciaNodosByServicioAndPunto(servicioDistancia,matrizDistancia,nodoCodigo);
    }

    public DistanciaNodos getDistanciaNodosByServicioAndNodo(ServicioDistancia servicioDistancia, MatrizDistancia matrizDistancia,String nodoCodigo){
        return distanciaNodosDao.getDistanciaNodosByServicioAndNodo(servicioDistancia,matrizDistancia,nodoCodigo);
    }

    @Transactional(readOnly = false)
    public void addListaNegraMatriz(ListaNegraMatriz listaNegraMatriz) {
        listaNegraMatrizDao.addListaNegraMatriz(listaNegraMatriz);
    }

    @Transactional(readOnly = false)
    public void deleteListaNegraMatriz(ListaNegraMatriz listaNegraMatriz) {
        listaNegraMatrizDao.deleteListaNegraMatriz(listaNegraMatriz);
    }

    @Transactional(readOnly = false)
    public void updateListaNegraMatriz(ListaNegraMatriz listaNegraMatriz) {
        listaNegraMatrizDao.updateListaNegraMatriz(listaNegraMatriz);
    }


    public List<ListaNegraMatriz> getListaNegraMatrizAll() {
        return listaNegraMatrizDao.getListaNegraMatrizAll();
    }

    public DistanciaNodos getUltimoDistanciaNodosByServicioAndPunto(ServicioDistancia servicioDistancia, MatrizDistancia matrizDistancia){
        return distanciaNodosDao.getUltimoDistanciaNodosByServicioAndPunto(servicioDistancia,matrizDistancia);
    }

}
