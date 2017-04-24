package com.tmModulos.modelo.dao.saeBogota;

import com.tmModulos.modelo.entity.saeBogota.Lineas;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class LineasDao implements Serializable {

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

    public List<Lineas> getLineasByMacroAndLinea(int macro,int linea) {
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(Lineas.class);
        criteria.add(Restrictions.eq("macro",macro));
        criteria.add(Restrictions.eq("linea",linea));
        List list = criteria.list();
        session.close();
        return list;
    }
}
