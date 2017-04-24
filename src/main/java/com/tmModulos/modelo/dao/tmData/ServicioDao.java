package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Servicio;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ServicioDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addServicio(Servicio servicio) {
        Serializable save = getSessionFactory().getCurrentSession().save(servicio);

    }

    public void deleteServicio(Servicio servicio) {
        getSessionFactory().getCurrentSession().delete(servicio);
    }


    public void updateServicio(Servicio servicio) {
        getSessionFactory().getCurrentSession().update(servicio);
    }


    public List<Servicio> getServicioAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Servicio ").list();
        return list;
    }

    public Servicio getServicioBymacroLineaYseccion(int macro,int linea,int seccion, int nodo){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Servicio.class);
        criteria.add(Restrictions.eq("macro", macro));
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("seccion", seccion));
        criteria.add(Restrictions.eq("punto", nodo));
        List<Servicio> list = criteria.list();
        if(list.size()<1){
            return null;
        }
        return list.get(0);
    }

    public Servicio getServicioByTrayecto(int trayecto, int punto){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Servicio.class);
        criteria.add(Restrictions.eq("trayecto", trayecto));
        criteria.add(Restrictions.eq("punto", punto));
        List<Servicio> list = criteria.list();
        if(list.size()<1){
            return null;
        }
        return list.get(0);
    }

    public Servicio getServicioByNombreEspecial(String nombre){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Servicio.class);
        criteria.add(Restrictions.eq("nombreEspecial", nombre));
        List<Servicio> list = criteria.list();
        if(list.size()<1){
            return null;
        }
        return list.get(0);
    }

    public Servicio getServicioByIdentificador(String identificador){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Servicio.class);
        criteria.add(Restrictions.eq("identificador", identificador));
        List<Servicio> list = criteria.list();
        if(list.size()<1){
            return null;
        }
        return list.get(0);
    }
}
