package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.TipoDia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoDiaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTipoDia(TipoDia tipoDia) {
        getSessionFactory().getCurrentSession().save(tipoDia);
    }

    public void deleteTipoDia(TipoDia tipoDia) {
        getSessionFactory().getCurrentSession().delete(tipoDia);
    }


    public void updateTipoDia(TipoDia tipoDia) {
        getSessionFactory().getCurrentSession().update(tipoDia);
    }


    public List<TipoDia> getTipoDiaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TipoDia").list();
        return list;
    }

    public TipoDia getTipoDia(String nombre){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TipoDia.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        List list = criteria.list();
        if( list.size() > 0 ){
            return (TipoDia) list.get(0);
        }
        return null;
    }

}
