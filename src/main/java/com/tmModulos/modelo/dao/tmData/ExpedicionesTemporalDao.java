package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Equivalencias;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import com.tmModulos.modelo.entity.tmData.MatrizTemporal;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ExpedicionesTemporalDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addExpTemporal(ExpedicionesTemporal temporal) {
        Serializable save = getSessionFactory().getCurrentSession().save(temporal);

    }

    public void deleteExpTemporal(ExpedicionesTemporal temporal) {
        getSessionFactory().getCurrentSession().delete(temporal);
    }


    public void updateExpTemporal(ExpedicionesTemporal temporal) {
        getSessionFactory().getCurrentSession().update(temporal);
    }


    public List<ExpedicionesTemporal> getExpTemporalAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  ExpedicionesTemporal").list();
        return list;
    }

    public List<ExpedicionesTemporal> getExpedicionesTemporalsData(String id){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ExpedicionesTemporal.class);
        criteria.add(Restrictions.eq("identificador", id));
        criteria.addOrder(Order.asc("dia"));
        criteria.addOrder(Order.asc("instInicio"));
        return criteria.list();
    }

    public List<String> getExpedicionesNoReferenciadas(List<String> serviciosEncontrados){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ExpedicionesTemporal.class);
        criteria.add(Restrictions.not(Restrictions.in("identificador",serviciosEncontrados)));
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("identificador")));
        return criteria.list();
    }

//    private SimpleExpression getRestrictionList(List<String> serviciosEncontrados) {
//        List<Criterion> notIn = new ArrayList<Criterion>();
//        if(serviciosEncontrados.size()>0){
//
//            for (String id:serviciosEncontrados) {
//                notIn.add(Restrictions.("identificador",id));
//            }
//        }
//        return Restrictions.or(notIn);
//    }
}
