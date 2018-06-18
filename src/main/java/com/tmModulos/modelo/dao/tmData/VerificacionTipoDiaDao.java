package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Vagon;
import com.tmModulos.modelo.entity.tmData.VerificacionTipoDia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Repository
@Transactional
public class VerificacionTipoDiaDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addVerificacionTipoDia(VerificacionTipoDia tipoDia) {
        Serializable save = getSessionFactory().getCurrentSession().save(tipoDia);

    }

    public void deleteVerificacionTipoDia(VerificacionTipoDia tipoDia) {
        getSessionFactory().getCurrentSession().delete(tipoDia);
    }


    public void updateVerificacionTipoDia(VerificacionTipoDia tipoDia) {
        getSessionFactory().getCurrentSession().update(tipoDia);
    }


    public List<VerificacionTipoDia> getTipoDiaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  VerificacionTipoDia ").list();
        return list;
    }

    public VerificacionTipoDia getTipoDia(String tipoDia) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(VerificacionTipoDia.class);
        criteria.add(Restrictions.eq("tipoDia", tipoDia));
        return (VerificacionTipoDia) criteria.uniqueResult();
    }
}
