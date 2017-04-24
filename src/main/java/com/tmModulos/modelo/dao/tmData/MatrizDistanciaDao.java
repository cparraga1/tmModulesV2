package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.GisCarga;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
public class MatrizDistanciaDao {


    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addMatrizDistancia(MatrizDistancia matrizDistancia) {
        Serializable save = getSessionFactory().getCurrentSession().save(matrizDistancia);

    }

    public void deleteMatrizDistancia(MatrizDistancia matrizDistancia) {
        getSessionFactory().getCurrentSession().delete(matrizDistancia);
    }


    public void updateMatrizDistancia(MatrizDistancia matrizDistancia) {
        getSessionFactory().getCurrentSession().update(matrizDistancia);
    }


    public List<MatrizDistancia> getMatrizDistanciaAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  MatrizDistancia").list();
        return list;
    }

    public List<MatrizDistancia> getMatrizDistanciaByFecha(String tipoFecha,Date fecha){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MatrizDistancia.class);
        criteria.add(Restrictions.eq(tipoFecha, fecha));
        return criteria.list();
    }

    public List<MatrizDistancia> getMatrizDistanciaBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MatrizDistancia.class);
        criteria.add(  Restrictions.between(tipoFecha, fechaIni, fechaFin)  );
        return criteria.list();
    }

    public MatrizDistancia getMatrizDistanciaById(String id){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(MatrizDistancia.class);
        criteria.add(Restrictions.eq("numeracion", id));
        return (MatrizDistancia) criteria.uniqueResult();
    }
}
