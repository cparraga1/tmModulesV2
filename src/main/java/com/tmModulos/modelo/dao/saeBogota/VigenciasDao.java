package com.tmModulos.modelo.dao.saeBogota;

import com.tmModulos.modelo.entity.saeBogota.Vigencias;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
public class VigenciasDao implements Serializable {


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

    public List<Vigencias> getVigenciasDaoByDate(Date date) {

//        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
//        String myDate = "17-09-2016";
//        // Create date 17-04-2011 - 00h00
//        Date minDate = null;
//        try {
//            minDate = formatter.parse(myDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(Vigencias.class);
        criteria.add(Restrictions.eq("fecha", date));
        List list = criteria.list();
        session.close();
        return list;
    }
}
