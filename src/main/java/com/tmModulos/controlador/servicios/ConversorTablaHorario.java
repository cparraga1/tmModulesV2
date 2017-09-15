package com.tmModulos.controlador.servicios;

import com.tmModulos.modelo.dao.tmData.ServicioTipoDiaDao;
import com.tmModulos.modelo.dao.tmData.TempPosDao;
import com.tmModulos.modelo.dao.tmData.TipoDiaDao;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ConversorTablaHorario")
public class ConversorTablaHorario {

    @Autowired
    public TempPosDao tempHorarioDao;


    @Autowired
    private TipoDiaDao tipoDiaDao;

    @Autowired
    private ServicioTipoDiaDao servicioTipoDiaDao;



    public void deleteTablaHorario(){
        tempHorarioDao.deleteTablaHorario();
    }

    public void addTablaHorarioFromFile(String filename) throws Exception {

        try{
            tempHorarioDao.addTablaHorarioFromFile(filename);
        }catch (Exception e){
            throw new Exception("Archivo con Informacion no esperada");
        }
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
}
