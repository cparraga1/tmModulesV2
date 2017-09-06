package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.TipoFranjaDao;
import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("franjaHorarioService")
@Transactional(readOnly = true)
public class FranjaHorarioService {

    @Autowired
    TipoFranjaDao tipoFranjaDao;

    public List<TipoFranja> getAllTipoFranja(){
        return tipoFranjaDao.getTipoFranjaAll();
    }

    @Transactional(readOnly = false)
    public void updateTipoFranja(TipoFranja tipoFranja){
        tipoFranjaDao.updateTipoFranja(tipoFranja);
    }

}
