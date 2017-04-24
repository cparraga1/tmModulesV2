package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.ArcoTiempo;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.GisServicio;
import com.tmModulos.modelo.entity.tmData.Servicio;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArcoTiempoDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addArcoTiempo(ArcoTiempo arcoTiempo) {
        getSessionFactory().getCurrentSession().save(arcoTiempo);
    }

    public void deleteArcoTiempo(ArcoTiempo arcoTiempo) {
        getSessionFactory().getCurrentSession().delete(arcoTiempo);
    }


    public void updateArcoTiempo(ArcoTiempo arcoTiempo) {
        getSessionFactory().getCurrentSession().update(arcoTiempo);
    }


    public List<ArcoTiempo> getArcoTiempoAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  ArcoTiempo ").list();
        return list;
    }

    public List<ArcoTiempo> getArcoTiempoByGisCarga(GisCarga gisCarga){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ArcoTiempo.class);
        criteria.add(Restrictions.eq("gisCargaArco", gisCarga));
        return criteria.list();
    }

    public List<ArcoTiempo> getArcoTiempoByGisCargaAndServicio(GisCarga gisCarga,GisServicio servicio){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ArcoTiempo.class);
        criteria.add(Restrictions.eq("gisCargaArco", gisCarga));
        criteria.add(Restrictions.eq("servicio", servicio));
        return criteria.list();
    }

    public List<ArcoTiempo> getArcoTiempoById(long id){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ArcoTiempo.class);
        criteria.add(Restrictions.eq("gisCargaArco", id));
        return criteria.list();
    }


}
