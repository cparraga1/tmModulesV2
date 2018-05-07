package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.ExpedicionesTempConv;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Repository
@Transactional
public class ExpedicionesTempConvDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addExpTemporal(ExpedicionesTempConv temporal) {
        Serializable save = getSessionFactory().getCurrentSession().save(temporal);

    }

}
