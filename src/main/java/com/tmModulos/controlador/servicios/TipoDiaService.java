package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.TipoDiaDao;
import com.tmModulos.modelo.dao.tmData.TipoDiaDetalleDao;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import com.tmModulos.modelo.entity.tmData.TipoDiaDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TipoDiaService {

    @Autowired
    TipoDiaDao tipoDiaDao;

    @Autowired
    TipoDiaDetalleDao tipoDiaDetalleDao;

    @Transactional(readOnly = false)
    public void addTipoDia(TipoDia tipoDia) { tipoDiaDao.addTipoDia(tipoDia);    }

    public void deleteTipoDia(TipoDia tipoDia) {
        tipoDiaDao.deleteTipoDia(tipoDia);
    }

    public void updateTipoDia(TipoDia tipoDia) {
        tipoDiaDao.updateTipoDia(tipoDia);
    }

    public List<TipoDia> getTipoDiaAll() {  return tipoDiaDao.getTipoDiaAll(); }

    public TipoDia getTipoDia(String nombre){ return tipoDiaDao.getTipoDia( nombre );}

    @Transactional(readOnly = false)
    public void addTipoDiaDetalle(TipoDiaDetalle  tipoDiaDetalle) { tipoDiaDetalleDao.addTipoDiaDetalle(tipoDiaDetalle);    }

    public void deleteTipoDiaDetalle(TipoDiaDetalle  tipoDiaDetalle) {
        tipoDiaDetalleDao.deleteTipoDiaDetalle(tipoDiaDetalle);
    }

    public void updateTipoDiaDetalle(TipoDiaDetalle  tipoDiaDetalle) {
        tipoDiaDetalleDao.updateTipoDiaDetalle(tipoDiaDetalle);
    }

    public List<TipoDiaDetalle> getTipoDiaDetalleAll() {  return tipoDiaDetalleDao.getTipoDiaDetalleAll(); }

    public List<TipoDiaDetalle> getTipoDiaByDetalle(String nombre){ return tipoDiaDetalleDao.getTipoDiaByDetalle( nombre );}
}
