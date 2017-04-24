package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Servicio;
import com.tmModulos.modelo.entity.tmData.ServicioTipoDia;
import com.tmModulos.modelo.entity.tmData.TablaMaestraServicios;
import com.tmModulos.modelo.entity.tmData.TipoDia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class ServicioTipoDiaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addServicio(ServicioTipoDia servicio) {
        Serializable save = getSessionFactory().getCurrentSession().save(servicio);

    }

    public void deleteServicioTipoDia(ServicioTipoDia servicio) {
        getSessionFactory().getCurrentSession().delete(servicio);
    }

    public void updateServicioTipoDia(ServicioTipoDia servicio) {
        getSessionFactory().getCurrentSession().update(servicio);
        getSessionFactory().getCurrentSession().flush();

    }

    public void updateListaServicioTipoDia(List<ServicioTipoDia> servicios) {
        for(ServicioTipoDia servicio:servicios){
            getSessionFactory().getCurrentSession().update(servicio);
        }
    }

    public List<ServicioTipoDia> getTServiciosAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  ServicioTipoDia").list();
        return list;
    }

    public List<ServicioTipoDia> getServiciosByTipoDia(TipoDia tipoDia){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ServicioTipoDia.class);
        criteria.add(Restrictions.eq("tipoDia", tipoDia));
        criteria.addOrder(Order.asc("orden"));
       return criteria.list();
    }

    public List<ServicioTipoDia> getServiciosById(String Identificador){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ServicioTipoDia.class);
        criteria.add(Restrictions.eq("identificador", Identificador));
        return criteria.list();
    }

    public List<ServicioTipoDia> getServiciosByServicio(Servicio servicio){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(ServicioTipoDia.class);
        criteria.add(Restrictions.eq("servicio", servicio));
        return criteria.list();
    }



}
