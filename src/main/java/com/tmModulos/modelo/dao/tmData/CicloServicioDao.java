package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.CicloServicio;
import com.tmModulos.modelo.entity.tmData.TablaMaestraServicios;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CicloServicioDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addCicloServicio(CicloServicio cicloServicio) {
        getSessionFactory().getCurrentSession().save(cicloServicio);
    }

    public void deleteCicloServicio(CicloServicio cicloServicio) {
        getSessionFactory().getCurrentSession().delete(cicloServicio);
    }


    public void updateCicloServicio(CicloServicio cicloServicio) {
        getSessionFactory().getCurrentSession().update(cicloServicio);
    }


    public List<CicloServicio> getCicloServiciosAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  CicloServicio").list();
        return list;
    }

    public List<CicloServicio> getCicloServicioByTabla(TablaMaestraServicios tablaMaestra){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(CicloServicio.class);
        criteria.add(Restrictions.eq("", tablaMaestra));
        return criteria.list();
    }

}
