package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.TipoDiaDetalle;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoDiaDetalleDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTipoDiaDetalle(TipoDiaDetalle tipoDiaDetalle) {
        getSessionFactory().getCurrentSession().save(tipoDiaDetalle);
    }

    public void deleteTipoDiaDetalle(TipoDiaDetalle tipoDiaDetalle) {
        getSessionFactory().getCurrentSession().delete(tipoDiaDetalle);
    }


    public void updateTipoDiaDetalle(TipoDiaDetalle tipoDiaDetalle) {
        getSessionFactory().getCurrentSession().update(tipoDiaDetalle);
    }


    public List<TipoDiaDetalle> getTipoDiaDetalleAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TipoDiaDetalle").list();
        return list;
    }

    public List<TipoDiaDetalle> getTipoDiaByDetalle(String nombre){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TipoDiaDetalle.class);
        criteria.add(Restrictions.eq("detalle", nombre));
        return criteria.list();
    }
}
