package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.HoraFranja;
import com.tmModulos.modelo.entity.tmData.IntervalosProgramacion;
import com.tmModulos.modelo.entity.tmData.TipoFranja;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HoraFranjaDao {
    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<HoraFranja> getHoraByFranja(TipoFranja tipoFranja){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(HoraFranja.class);
        criteria.add(Restrictions.eq("tipoFranja", tipoFranja));
        return criteria.list();
    }
}


