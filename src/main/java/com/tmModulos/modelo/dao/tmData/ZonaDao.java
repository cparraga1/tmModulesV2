package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Zona;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ZonaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addZona(Zona zona) {
        getSessionFactory().getCurrentSession().save(zona);
    }

    public void deleteZona(Zona zona) {
        getSessionFactory().getCurrentSession().delete(zona);
    }


    public void updateZona(Zona zona) {
        getSessionFactory().getCurrentSession().update(zona);
    }


    public List<Zona> getZonaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Zona").list();
        return list;
    }

    public List<Zona> getZonaByTipoZona(String tipoZona) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Zona.class);
        criteria.add(Restrictions.eq("tipoZona", tipoZona));
        return criteria.list();
    }

    public Zona getNombreByNombre(String nombre,String tipozona){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Zona.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        criteria.add(Restrictions.eq("tipoZona", tipozona));
        return (Zona) criteria.uniqueResult();
    }


}
