package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class HorarioDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void addHorarios(Horario horariosServicio) {
        getSessionFactory().getCurrentSession().save(horariosServicio);
    }

    public void deleteHorarios(Horario horariosServicio) {
        getSessionFactory().getCurrentSession().delete(horariosServicio);
    }


    public void updateHorarios(Horario horariosServicio) {
        getSessionFactory().getCurrentSession().update(horariosServicio);
    }


    public List<Horario> getHorariosByServicio(Servicio servicio){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Horario.class);
        criteria.add(Restrictions.eq("servicio", servicio));
        return criteria.list();
    }

    public List<Horario> getHorariosByServicioAndTipoDio(Servicio servicio,TipoDia tipoDia){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Horario.class);
        criteria.add(Restrictions.eq("servicio", servicio));
        criteria.add(Restrictions.eq("tipoDia", tipoDia));
        criteria.addOrder(Order.asc("horaInicio"));
        return criteria.list();
    }

    public List<Horario> getHorariosByServicio(Servicio servicio, TipoDia dia) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Horario.class);
        criteria.add(Restrictions.eq("servicio", servicio));
        criteria.add(Restrictions.eq("tipoDia", dia));
        criteria.add(Restrictions.eq("tipoHorario", "P"));
        criteria.addOrder(Order.asc("horaInicio"));
        //criteria.addOrder(Order.asc("config"));
        return criteria.list();
    }

    public List<Horario> getHorarioByTipoDia(TipoDia aDuplicar) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Horario.class);
        criteria.add(Restrictions.eq("tipoDia", aDuplicar));
        criteria.add(Restrictions.eq("tipoHorario", "P"));
        return criteria.list();
    }
}
