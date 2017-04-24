package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.IntervalosProgramacion;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Repository
public class IntervalosProgramacionDao implements Serializable {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public IntervalosProgramacion getIntervaloForDate(Time time){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(IntervalosProgramacion.class);
        criteria.add(Restrictions.ge("fin", time));
        criteria.add(Restrictions.le("inicio", time));
        List<IntervalosProgramacion> intervalos =criteria.list();
        if(intervalos.size()>=2){
            return intervalos.get(1);
        }
        return intervalos.get(0);
    }

    public List<IntervalosProgramacion> getIntervaloByFranja(TipoFranja tipoFranja){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(IntervalosProgramacion.class);
        criteria.add(Restrictions.eq("tipoFranja", tipoFranja));
        return criteria.list();
    }

    public List<IntervalosProgramacion> getAllIntervalos(){
        List list = getSessionFactory().getCurrentSession().createQuery("from  IntervalosProgramacion").list();
        return list;
    }
}
