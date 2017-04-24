package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.FranjaHoraria;
import com.tmModulos.modelo.entity.tmData.Tipologia;
import com.tmModulos.modelo.entity.tmData.Zona;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FranjaHorariaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addFranjaHoraria(FranjaHoraria franjaHoraria) {
        getSessionFactory().getCurrentSession().save(franjaHoraria);
    }

    public void deleteFranjaHoraria(FranjaHoraria franjaHoraria) {
        getSessionFactory().getCurrentSession().delete(franjaHoraria);
    }


    public void updateFranjaHoraria(FranjaHoraria franjaHoraria) {
        getSessionFactory().getCurrentSession().update(franjaHoraria);
    }


    public List<FranjaHoraria> getFranjaHorariaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  FranjaHoraria").list();
        return list;
    }
}
