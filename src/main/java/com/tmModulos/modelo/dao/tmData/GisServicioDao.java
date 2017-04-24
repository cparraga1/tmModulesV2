package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.ArcoTiempo;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.GisServicio;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GisServicioDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addGisServicio(GisServicio gisServicio) {
        getSessionFactory().getCurrentSession().save(gisServicio);
    }

    public void deleteGisServicio(GisServicio gisServicio) {
        getSessionFactory().getCurrentSession().delete(gisServicio);
    }


    public void updateGisServicio(GisServicio gisServicio) {
        getSessionFactory().getCurrentSession().update(gisServicio);
    }


    public List<GisServicio> getGisServicioAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  GisServicio ").list();
        return list;
    }

    public GisServicio getGisServicioByTrayectoLinea(String identificador){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(GisServicio.class);
        criteria.add(Restrictions.eq("identificador", identificador));
        return (GisServicio) criteria.uniqueResult();
    }


}
