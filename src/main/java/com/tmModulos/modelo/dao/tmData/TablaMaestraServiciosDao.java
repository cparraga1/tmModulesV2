package com.tmModulos.modelo.dao.tmData;


import com.tmModulos.modelo.entity.tmData.DistanciaNodos;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.TablaMaestra;
import com.tmModulos.modelo.entity.tmData.TablaMaestraServicios;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TablaMaestraServiciosDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTServicios(TablaMaestraServicios tablaMaestraServicios) {
        getSessionFactory().getCurrentSession().save(tablaMaestraServicios);
    }

    public void deleteTServicios(TablaMaestraServicios tablaMaestraServicios) {
        getSessionFactory().getCurrentSession().delete(tablaMaestraServicios);
    }


    public void updateTServicios(TablaMaestraServicios tablaMaestraServicios) {
        getSessionFactory().getCurrentSession().update(tablaMaestraServicios);
    }


    public List<TablaMaestraServicios> getTServicios() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TablaMaestraServicios").list();
        return list;
    }

    public List<TablaMaestraServicios> getServiciosByTabla(TablaMaestra tablaMaestra){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TablaMaestraServicios.class);
        criteria.add(Restrictions.eq("tablaMeestra", tablaMaestra));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

}
