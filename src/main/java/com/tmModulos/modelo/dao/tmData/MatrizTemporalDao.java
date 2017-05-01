package com.tmModulos.modelo.dao.tmData;


import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.MatrizTemporal;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class MatrizTemporalDao {


    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addIntervalosTemporal(MatrizTemporal intervaloTemporal) {
        Serializable save = getSessionFactory().getCurrentSession().save(intervaloTemporal);

    }

    public void deleteIntervalosTemporal(MatrizTemporal intervaloTemporal) {
        getSessionFactory().getCurrentSession().delete(intervaloTemporal);
    }


    public void updateIntervalosTemporal(MatrizTemporal intervaloTemporal) {
        getSessionFactory().getCurrentSession().update(intervaloTemporal);
    }


    public List<MatrizTemporal> getIntervalosTemporalAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  MatrizTemporal").list();
        return list;
    }
}
