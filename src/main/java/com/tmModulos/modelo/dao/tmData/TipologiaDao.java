package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Tipologia;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipologiaDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTipologia(Tipologia tipologia) {
        getSessionFactory().getCurrentSession().save(tipologia);
    }

    public void deleteTipologia(Tipologia tipologia) {
        getSessionFactory().getCurrentSession().delete(tipologia);
    }


    public void updateTipologia(Tipologia tipologia) {
        getSessionFactory().getCurrentSession().update(tipologia);
    }


    public List<Tipologia> getTipologiaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Tipologia").list();
        return list;
    }

    public Tipologia getTipologiaByNombre(String nombre){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Tipologia.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        return (Tipologia) criteria.uniqueResult();
    }
}
