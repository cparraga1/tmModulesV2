package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.GisServicio;
import com.tmModulos.modelo.entity.tmData.HorariosServicio;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HorariosServicioDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addHorariosServicios(HorariosServicio horariosServicio) {
        getSessionFactory().getCurrentSession().save(horariosServicio);
    }

    public void deleteHorariosServicios(HorariosServicio horariosServicio) {
        getSessionFactory().getCurrentSession().delete(horariosServicio);
    }


    public void updateHorariosServicios(HorariosServicio horariosServicio) {
        getSessionFactory().getCurrentSession().update(horariosServicio);
    }


    public List<GisServicio> getHorariosServiciosAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  HorariosServicio ").list();
        return list;
    }


}
