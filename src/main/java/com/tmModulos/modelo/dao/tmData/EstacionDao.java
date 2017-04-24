package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Estacion;
import com.tmModulos.modelo.entity.tmData.Nodo;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EstacionDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addEstacion(Estacion estacion) {
        getSessionFactory().getCurrentSession().save(estacion);
    }

    public void deleteEstacion(Estacion estacion) {
        getSessionFactory().getCurrentSession().delete(estacion);
    }


    public void updateEstacion(Estacion estacion) {
        getSessionFactory().getCurrentSession().update(estacion);
    }


    public List<Estacion> getEstacionAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Estacion").list();
        return list;
    }

    public List<Estacion> getEstacionbyCodigo(Integer codigo){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Estacion.class);
        criteria.add(Restrictions.eq("codigo", codigo));
        return criteria.list();
    }

}
