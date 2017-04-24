package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.ServicioDistancia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServicioDistanciaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addServicioDistancia(ServicioDistancia servicioDistancia) {
        getSessionFactory().getCurrentSession().save(servicioDistancia);
    }

    public void deleteServicioDistancia(ServicioDistancia servicioDistancia) {
        getSessionFactory().getCurrentSession().delete(servicioDistancia);
    }


    public void updateServicioDistancia(ServicioDistancia servicioDistancia) {
        getSessionFactory().getCurrentSession().update(servicioDistancia);
    }


    public List<ServicioDistancia> getServicioDistanciaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  ServicioDistancia ").list();
        return list;
    }

    public ServicioDistancia getServicioDistanciaByMacroLineaSeccion(int macro, int linea, int seccion){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ServicioDistancia.class);
        criteria.add(Restrictions.eq("macro", macro));
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("seccion", seccion));
        List<ServicioDistancia> lista = criteria.list();
        if(lista.size()>0){
            return lista.get(0);
        }
        return null;
    }

    public ServicioDistancia getServicioDistanciaByIdentificador(String identificador){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ServicioDistancia.class);
        criteria.add(Restrictions.eq("identificador", identificador));
        return (ServicioDistancia) criteria.uniqueResult();
    }
}
