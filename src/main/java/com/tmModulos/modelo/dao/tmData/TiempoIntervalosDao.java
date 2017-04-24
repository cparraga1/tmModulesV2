package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class TiempoIntervalosDao implements Serializable {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTiempoIntervalos(TiempoIntervalos tiempoIntervalos) {
        Serializable save = getSessionFactory().getCurrentSession().save(tiempoIntervalos);
    }

    public List<TiempoIntervalos> getTiempoIntervalosByServicio(List<IntervalosProgramacion> intervalos, ServicioTipoDia id,GisIntervalos gisIntervalos){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TiempoIntervalos.class);
        Disjunction or = Restrictions.disjunction();
        if(intervalos!=null){
            for (IntervalosProgramacion intervalo: intervalos ) {
                or.add(Restrictions.eq("intervalosProgramacion", intervalo));
            }
            criteria.add(or);
        }

        criteria.add(Restrictions.eq("idServicio",id));
        criteria.add(Restrictions.eq("gisIntervalos",gisIntervalos));
        criteria.addOrder(Order.asc("instante"));
        return criteria.list();
    }

    public List<TiempoIntervalos> getTiempoIntervalosByHoraServicioCuadro
            (IntervalosProgramacion intervalo, ServicioTipoDia tipoDia,String cuadro){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TiempoIntervalos.class);
        criteria.add(Restrictions.eq("idServicio",tipoDia));
        criteria.add(Restrictions.eq("intervalosProgramacion",intervalo));
        return criteria.list();
    }
}
