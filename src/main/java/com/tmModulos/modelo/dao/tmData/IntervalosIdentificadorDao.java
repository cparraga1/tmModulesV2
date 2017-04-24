package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.IntervalosIdentificador;
import com.tmModulos.modelo.entity.tmData.IntervalosProgramacion;
import com.tmModulos.modelo.entity.tmData.TiempoIntervalos;
import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class IntervalosIdentificadorDao implements Serializable {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addIntervalosId(IntervalosIdentificador intervalosIdentificador) {
        Serializable save = getSessionFactory().getCurrentSession().save(intervalosIdentificador);
            }


    public IntervalosIdentificador getIntervalosIdentificadorByServicio(String id,String cuadro){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(IntervalosIdentificador.class);
        criteria.add(Restrictions.eq("identificador", id));
        criteria.add(Restrictions.eq("cuadro", cuadro));
        return (IntervalosIdentificador) criteria.uniqueResult();
    }

    public List<IntervalosIdentificador> getAllIntervalosIdByCuadro(String cuadro){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(IntervalosIdentificador.class);
        criteria.add(Restrictions.eq("cuadro", cuadro));
        return criteria.list();
    }
}