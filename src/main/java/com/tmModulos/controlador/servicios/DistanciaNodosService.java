package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.saeBogota.*;
import com.tmModulos.modelo.entity.saeBogota.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("DistanciaNodosService")
@Transactional(readOnly = true)
public class DistanciaNodosService {


    @Autowired
    NodosDao nodosDao;

    @Autowired
    VigenciasDao vigenciasDao;

    @Autowired
    LineasDao lineasDao;

    @Autowired
    SeccionesDao seccionesDao;

    @Autowired
    NodosSeccionDao nodosSeccionDao;

    public List<Nodos> getArcoTiempoAll() {
        return nodosDao.getArcoTiempoAll();
    }

    public List<Vigencias> getVigenciasDaoByDate(Date date) {
        return vigenciasDao.getVigenciasDaoByDate( date );
    }

    public List<Lineas> getLineasByMacroAndLinea(int macro, int linea) {
        return lineasDao.getLineasByMacroAndLinea(macro,linea);
    }

    public List<Secciones> getSeccionesByMacroLineaAndConfig(int macro, int linea, int config, int seccion) {
        return  seccionesDao.getSeccionesByMacroLineaAndConfig( macro,linea,config,seccion );
    }

    public List<NodosSeccion> getNodosSeccionesByMacroLineaAndConfig(int macro, int linea,int seccion, int config, int tipoNodo) {
        return nodosSeccionDao.getNodosSeccionesByMacroLineaAndConfig(macro,linea,seccion,config,tipoNodo);
    }

    public Nodos getNodosByTipoandCode(int id,int tipo) {
        return nodosDao.getNodosByTipoandCode(id,tipo);
    }
}
