package com.tmModulos.modelo.dao.saeBogota;

import com.tmModulos.modelo.entity.saeBogota.Nodos;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class NodosDao implements Serializable {


    private SessionFactory sessionFactoryServer = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    Session session = null;
    Transaction transaction = null;
    Query query = null;;

    public SessionFactory getSessionFactoryServer() {
        return sessionFactoryServer;
    }

    public void setSessionFactoryServer(SessionFactory sessionFactoryServer) {
        this.sessionFactoryServer = sessionFactoryServer;
    }

    public List<Nodos> getArcoTiempoAll() {
        session = sessionFactoryServer.openSession();
        List list = session.createQuery("from  Nodos").list();
        session.close();
        return list;
    }

    public Nodos getNodosByTipoandCode(int id,int tipo) {
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(Nodos.class);
        criteria.add(Restrictions.eq("tipo",tipo));
        criteria.add(Restrictions.eq("id",id));
        Nodos nodos = (Nodos) criteria.uniqueResult();
        session.close();
        return nodos;
    }
}
