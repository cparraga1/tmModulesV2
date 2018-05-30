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

    public void deleteZona(Zona zonaSeleccionada) {
        zonaDao.deleteZona(zonaSeleccionada);
    }

    public void updateZona(Zona zonaSeleccionada) {
        zonaDao.updateZona(zonaSeleccionada);
    }

    public void addZona(Zona nuevaZona) {
        zonaDao.addZona(nuevaZona);
    }
}
