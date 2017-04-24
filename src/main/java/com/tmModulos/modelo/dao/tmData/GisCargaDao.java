package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.ArcoTiempo;
import com.tmModulos.modelo.entity.tmData.GisCarga;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
public class GisCargaDao {


    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addGisCarga(GisCarga gisCarga) {
        Serializable save = getSessionFactory().getCurrentSession().save(gisCarga);

    }

    public void deleteGisCarga(GisCarga gisCarga) {
        getSessionFactory().getCurrentSession().delete(gisCarga);
    }


    public void updateGisCarga(GisCarga gisCarga) {
        getSessionFactory().getCurrentSession().update(gisCarga);
    }


    public List<GisCarga> getGisCargaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  GisCarga").list();
        return list;
    }

    public List<GisCarga> getGisCargaByFecha(String tipoFecha,Date fecha){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(GisCarga.class);
        criteria.add(Restrictions.eq(tipoFecha, fecha));
        return criteria.list();
    }



    public List<GisCarga> getGisCargaBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(GisCarga.class);
        criteria.add(  Restrictions.between(tipoFecha, fechaIni, fechaFin)  );
        return criteria.list();
    }

    public GisCarga getGisCargaById(String id){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(GisCarga.class);
        criteria.add(Restrictions.eq("descripcion", id));
        return (GisCarga) criteria.uniqueResult();
    }
}
