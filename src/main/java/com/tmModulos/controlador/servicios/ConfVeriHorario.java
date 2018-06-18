package com.tmModulos.controlador.servicios;


import com.tmModulos.modelo.dao.tmData.VerificacionTipoDiaDao;
import com.tmModulos.modelo.entity.tmData.VerificacionTipoDia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Service("ConfService")
@Transactional(readOnly = true)
public class ConfVeriHorario {

    @Autowired
    VerificacionTipoDiaDao verificacionTipoDiaDao;

    public ConfVeriHorario() {
    }

    @Transactional(readOnly = false)
    public void addVerificacionTipoDia(VerificacionTipoDia tipoDia) {
       verificacionTipoDiaDao.addVerificacionTipoDia(tipoDia);

    }

    @Transactional(readOnly = false)
    public void deleteVerificacionTipoDia(VerificacionTipoDia tipoDia) {
        verificacionTipoDiaDao.deleteVerificacionTipoDia(tipoDia);
    }

    @Transactional(readOnly = false)
    public void updateVerificacionTipoDia(VerificacionTipoDia tipoDia) {
        verificacionTipoDiaDao.updateVerificacionTipoDia(tipoDia);
    }


    public List<VerificacionTipoDia> getTipoDiaAll() {
       return verificacionTipoDiaDao.getTipoDiaAll();
    }

    public VerificacionTipoDia getTipoDia(String tipoDia) {
        return verificacionTipoDiaDao.getTipoDia(tipoDia);
    }
}
