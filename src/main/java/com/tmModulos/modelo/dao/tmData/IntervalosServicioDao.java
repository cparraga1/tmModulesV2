package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.IntervalosServicio;
import com.tmModulos.modelo.entity.tmData.TablaMaestra;
import com.tmModulos.modelo.entity.tmData.TablaMaestraServicios;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IntervalosServicioDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addIntervalosServicio(IntervalosServicio intervalosServicio) {
        getSessionFactory().getCurrentSession().save(intervalosServicio);
    }

    public void deleteIntervalosServicios(IntervalosServicio intervalosServicio) {
        getSessionFactory().getCurrentSession().delete(intervalosServicio);
    }


    public void updateIntervalosServicio(IntervalosServicio intervalosServicio) {
        getSessionFactory().getCurrentSession().update(intervalosServicio);
    }


    public List<IntervalosServicio> getIntervalosServicioAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  IntervalosServicio").list();
        return list;
    }

    public List<IntervalosServicio> getIntervalosServicioByTabla(TablaMaestraServicios tablaMaestra){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(IntervalosServicio.class);
        criteria.add(Restrictions.eq("", tablaMaestra));
        return criteria.list();
    }

}
