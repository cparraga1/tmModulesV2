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

    public TipoFranja getTipoFranjaByHorario(String horaIncio,String horaFin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TipoFranja.class);
        criteria.add(Restrictions.eq("horaInicio", horaIncio));
        criteria.add(Restrictions.eq("horaFin", horaFin));
        return (TipoFranja) criteria.uniqueResult();
    }

    public TipoFranja getTipoFranjaByNombre(String nombre){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TipoFranja.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        return (TipoFranja) criteria.uniqueResult();
    }

}
