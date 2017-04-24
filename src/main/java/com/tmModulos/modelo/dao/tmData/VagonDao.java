package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Estacion;
import com.tmModulos.modelo.entity.tmData.Vagon;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VagonDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addVagon(Vagon vagon) {
        getSessionFactory().getCurrentSession().save(vagon);
    }

    public void deleteVagon(Vagon vagon) {
        getSessionFactory().getCurrentSession().delete(vagon);
    }


    public void updateVagon(Vagon vagon) {
        getSessionFactory().getCurrentSession().update(vagon);
    }


    public List<Vagon> getVagonAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Vagon").list();
        return list;
    }

    public List<Vagon> getVagonbyEstacion(Estacion estacion){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Vagon.class);
        criteria.add(Restrictions.eq("estacion", estacion));
        return criteria.list();
    }

    public Vagon getVagonbyId(Long id){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Vagon.class);
        criteria.add(Restrictions.eq("id", id));
        return (Vagon) criteria.uniqueResult();
    }
}
