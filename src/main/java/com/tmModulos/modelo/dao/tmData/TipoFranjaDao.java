package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoFranjaDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addTipoFranja(TipoFranja tipoFranja) {
        getSessionFactory().getCurrentSession().save(tipoFranja);
    }

    public void deleteTipoFranja(TipoFranja tipoFranja) {
        getSessionFactory().getCurrentSession().delete(tipoFranja);
    }


    public void updateTipoFranja(TipoFranja tipoFranja) {
        getSessionFactory().getCurrentSession().update(tipoFranja);
    }


    public List<TipoFranja> getTipoFranjaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TipoFranja").list();
        return list;
    }

    public List<TipoFranja> getTipoFranjaByHorario(String horaInicio,String horaFin){

        List list = getSessionFactory().getCurrentSession().createQuery("from  TipoFranja where (horaInicio >= \'"+horaInicio+"\' AND horaFin <= \'" + horaFin + "\') OR (\'" + horaInicio + "\' BETWEEN horaInicio AND horaFin) OR (\'" + horaFin + "\' BETWEEN horaInicio AND horaFin)").list();
        return list;

        /*Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TipoFranja.class);
        criteria.add(Restrictions.or(
                Restrictions.or(Restrictions.eq("horaInicio", horaInicio), Restrictions.eq("horaFin", horaFin)),
                Restrictions.and(Restrictions.gt("horaInicio", horaInicio), Restrictions.lt("horaFin", horaFin)))
        );
        return criteria.list();*/
    }



    public TipoFranja getTipoFranjaByNombre(String nombre){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TipoFranja.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        return (TipoFranja) criteria.uniqueResult();
    }

}
