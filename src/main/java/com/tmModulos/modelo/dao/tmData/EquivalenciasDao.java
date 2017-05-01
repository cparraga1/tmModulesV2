package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Equivalencias;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
@Transactional
public class EquivalenciasDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public List<Equivalencias> getEquivalenciasAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Equivalencias ").list();
        return list;
    }

    public List<Equivalencias> getEquivalenciasByData(int linea, int sublinea, int puntoI, int puntoF){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Equivalencias.class);
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("sublinea", sublinea));
        criteria.add(Restrictions.eq("puntoInicio", puntoI));
        criteria.add(Restrictions.eq("puntoFin", puntoF));
        return criteria.list();
    }

}
