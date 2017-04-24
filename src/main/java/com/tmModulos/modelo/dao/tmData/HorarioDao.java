package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

}
