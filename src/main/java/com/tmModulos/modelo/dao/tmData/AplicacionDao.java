package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Aplicacion;
import com.tmModulos.modelo.entity.tmData.ArcoTiempo;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AplicacionDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Aplicacion obtenerAplicacionById(int idAplicacion) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Aplicacion.class);
        criteria.add(Restrictions.eq("codigo", idAplicacion));
        return (Aplicacion) criteria.uniqueResult();
    }
}
