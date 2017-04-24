package com.tmModulos.modelo.dao.saeBogota;

import com.tmModulos.modelo.entity.saeBogota.NodosSeccion;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public class NodosSeccionDao implements Serializable {

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

    public List<NodosSeccion> getNodosSeccionesByMacroLineaAndConfig(int macro, int linea,int seccion, int config,int tipoNodo) {
        session = sessionFactoryServer.openSession();
//        int seccionAux = 3;
//        if(seccion==2){
//            seccionAux=4;
//        }
        Criteria criteria = session.createCriteria(NodosSeccion.class);
//        Criterion eventos= Restrictions.or(Restrictions.eq("seccion",seccion),
//                Restrictions.eq("evento", seccionAux));
//        criteria.add(eventos);
        criteria.add(Restrictions.eq("macro",macro));
        criteria.add(Restrictions.eq("linea",linea));
        criteria.add(Restrictions.eq("seccion",seccion));
        criteria.add(Restrictions.eq("configLinea",config));
        criteria.add(Restrictions.eq("tipo",tipoNodo));
        List list = criteria.list();
        session.close();
        return list;
    }
}
