package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.TiempoIntervalos;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
@Repository
public class TiempoIntervalosAuxiliarDao implements Serializable {

    private SessionFactory sessionFactoryServer = new Configuration().configure("hibernatePsql.cfg.xml").buildSessionFactory();
    Session session = null;
    Transaction transaction = null;
    Query query = null;;

    public SessionFactory getSessionFactoryServer() {
        return sessionFactoryServer;
    }

    public void setSessionFactoryServer(SessionFactory sessionFactoryServer) {
        this.sessionFactoryServer = sessionFactoryServer;
    }

    public void addTiempoIntervalos(List<TiempoIntervalos> tiempoIntervalos) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        for(TiempoIntervalos intervalo:tiempoIntervalos){
            session.save(intervalo);
        }

        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

}
