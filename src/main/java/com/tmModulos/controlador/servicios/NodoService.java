package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.EstacionDao;
import com.tmModulos.modelo.dao.tmData.NodoDao;
import com.tmModulos.modelo.dao.tmData.VagonDao;
import com.tmModulos.modelo.dao.tmData.ZonaDao;
import com.tmModulos.modelo.entity.tmData.Estacion;
import com.tmModulos.modelo.entity.tmData.Nodo;
import com.tmModulos.modelo.entity.tmData.Vagon;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("NodoService")
@Transactional(readOnly = true)
public class NodoService {

    @Autowired
    NodoDao nodoDao;

    @Autowired
    ZonaDao zonaDao;

    @Autowired
    EstacionDao estacionDao;

    @Autowired
    VagonDao vagonDao;

    @Transactional(readOnly = false)
    public void addNodo(Nodo nodo) { nodoDao.addNodo(nodo); }

    @Transactional(readOnly = false)
    public void deleteNodo(Nodo nodo) { nodoDao.deleteNodo(nodo);}

    @Transactional(readOnly = false)
    public void updateNodo(Nodo nodo) { nodoDao.updateNodo(nodo);}

    @Transactional(readOnly = true)
    public List<Nodo> getNodosAll() { return  nodoDao.getNodosAll(); }

    public Nodo getNodo(String nombre){ return nodoDao.getNodo( nombre );}

    public List<Nodo> getNodoByVagon(Vagon vagon){
        return nodoDao.getNodoByVagon(vagon);
    }

    public Nodo getNodoByCodigo(int codigo){ return nodoDao.getNodoByCodigo(codigo);}

    @Transactional(readOnly = false)
    public void addZona(Zona zona) {
         zonaDao.addZona(zona);
    }

    @Transactional(readOnly = false)
    public void deleteZona(Zona zona) {
        zonaDao.deleteZona(zona);
    }

    @Transactional(readOnly = false)
    public void updateZona(Zona zona) {
        zonaDao.updateZona(zona);
    }


    public List<Zona> getZonaAll() {
        return zonaDao.getZonaAll();
    }

    public List<Zona> getZonaByTipoZona(String tipoZona) {
        return zonaDao.getZonaByTipoZona(tipoZona);
    }

    public Zona getZonaByName(String nombre,String tipozona){
            return  zonaDao.getNombreByNombre(nombre,tipozona);
    }

    @Transactional(readOnly = false)
    public void addEstacion(Estacion estacion)
    {
        estacionDao.addEstacion(estacion);
    }

    @Transactional(readOnly = false)
    public void deleteEstacion(Estacion estacion) {
        estacionDao.deleteEstacion(estacion);
    }

    @Transactional(readOnly = false)
    public void updateEstacion(Estacion estacion) {
       estacionDao.updateEstacion(estacion);
    }


    public List<Estacion> getEstacionAll() {
        return estacionDao.getEstacionAll();
    }

    public List<Estacion> getEstacionbyCodigo(Integer codigo){
        return estacionDao.getEstacionbyCodigo(codigo);
    }

    @Transactional(readOnly = false)
    public void addVagon(Vagon vagon) {
       vagonDao.addVagon(vagon);
    }

    @Transactional(readOnly = false)
    public void deleteVagon(Vagon vagon) {
        vagonDao.deleteVagon(vagon);
    }

    @Transactional(readOnly = false)
    public void updateVagon(Vagon vagon) {
       vagonDao.updateVagon(vagon);
    }


    public List<Vagon> getVagonAll() {
        return vagonDao.getVagonAll();
    }

    public Vagon getVagonbyId(Long id){
        return vagonDao.getVagonbyId(id);
    }

    public List<Vagon> getVagonbyEstacion(Estacion estacion){
        return vagonDao.getVagonbyEstacion(estacion);
    }

}
