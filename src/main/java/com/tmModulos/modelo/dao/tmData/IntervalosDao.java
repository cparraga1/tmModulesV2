package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IntervalosDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addIntervalos(Intervalos intervalos) {
        getSessionFactory().getCurrentSession().save(intervalos);
    }

    public List<Intervalos> getIntervalosByGisIntervalos(GisIntervalos gisIntervalos){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Intervalos.class);
        criteria.add(Restrictions.eq("gisIntervalos", gisIntervalos));
        return criteria.list();
    }


}
