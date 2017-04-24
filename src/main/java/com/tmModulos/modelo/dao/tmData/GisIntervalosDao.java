package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.GisIntervalos;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
public class GisIntervalosDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addGisIntervalo(GisIntervalos gisIntervalos) {
        Serializable save = getSessionFactory().getCurrentSession().save(gisIntervalos);

    }

    public List<GisIntervalos> getGisIntervaloByFecha(String tipoFecha, Date fecha){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(GisIntervalos.class);
        criteria.add(Restrictions.eq(tipoFecha, fecha));
        return criteria.list();
    }



    public List<GisIntervalos> getGisIntervaloBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(GisIntervalos.class);
        criteria.add(  Restrictions.between(tipoFecha, fechaIni, fechaFin)  );
        return criteria.list();
    }

    public List<GisIntervalos> getGisIntervalosAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  GisIntervalos").list();
        return list;
    }
}
