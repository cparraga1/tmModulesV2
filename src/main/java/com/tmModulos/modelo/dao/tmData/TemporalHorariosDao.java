package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.TemporalHorarios;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public class TemporalHorariosDao implements Serializable {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTemporalHorarios(TemporalHorarios temporalHorarios) {
        Serializable save = getSessionFactory().getCurrentSession().save(temporalHorarios);

    }

    public void deleteTemporalbyCuadro(String cuadro) {
//        getSessionFactory().getCurrentSession().delete(matrizDistancia);
    }


}
