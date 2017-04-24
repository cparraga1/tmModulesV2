package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.CicloServicio;
import com.tmModulos.modelo.entity.tmData.VelocidadProgramada;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VelocidadProgramadaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addVelocidadProgramada(VelocidadProgramada velocidadProgramada) {
        getSessionFactory().getCurrentSession().save(velocidadProgramada);
    }

    public void deleteVelocidadProgramada(VelocidadProgramada velocidadProgramada) {
        getSessionFactory().getCurrentSession().delete(velocidadProgramada);
    }


    public void updateVelocidadProgramada(VelocidadProgramada velocidadProgramada) {
        getSessionFactory().getCurrentSession().update(velocidadProgramada);
    }


    public List<VelocidadProgramada> getVelocidadProgramadaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  VelocidadProgramada").list();
        return list;
    }


}
