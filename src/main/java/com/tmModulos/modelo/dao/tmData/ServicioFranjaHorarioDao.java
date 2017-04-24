package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.FranjaHoraria;
import com.tmModulos.modelo.entity.tmData.ServicioFranjaHorario;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServicioFranjaHorarioDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addServicioFranjaHorario(ServicioFranjaHorario servicioFranjaHorario) {
        getSessionFactory().getCurrentSession().save(servicioFranjaHorario);
    }

    public void deleteServicioFranjaHorario(ServicioFranjaHorario servicioFranjaHorario) {
        getSessionFactory().getCurrentSession().delete(servicioFranjaHorario);
    }


    public void updateServicioFranjaHorario(ServicioFranjaHorario servicioFranjaHorario) {
        getSessionFactory().getCurrentSession().update(servicioFranjaHorario);
    }


    public List<ServicioFranjaHorario> getServicioFranjaHorarioAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  ServicioFranjaHorario").list();
        return list;
    }
}
