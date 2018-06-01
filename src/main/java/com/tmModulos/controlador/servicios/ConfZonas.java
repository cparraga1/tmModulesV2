package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.ZonaDao;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ConfZonas")
@Transactional(readOnly = true)
public class ConfZonas {

    @Autowired
    ZonaDao zonaDao;

    public ConfZonas() {
    }



    public List<Zona> getAllZonas() {
        return zonaDao.getZonaAll();
    }

    @Transactional(readOnly = false)
    public void deleteZona(Zona zonaSeleccionada) {
        zonaDao.deleteZona(zonaSeleccionada);
    }

    @Transactional(readOnly = false)
    public void updateZona(Zona zonaSeleccionada) {
        zonaDao.updateZona(zonaSeleccionada);
    }

    @Transactional(readOnly = false)
    public void addZona(Zona nuevaZona) {
        zonaDao.addZona(nuevaZona);
    }
}
