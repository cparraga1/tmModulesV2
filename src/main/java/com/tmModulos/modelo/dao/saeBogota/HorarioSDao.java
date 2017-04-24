package com.tmModulos.modelo.dao.saeBogota;

import com.tmModulos.modelo.entity.saeBogota.HorarioS;
import com.tmModulos.modelo.entity.saeBogota.Vigencias;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class HorarioSDao implements Serializable {

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

    public List<HorarioS> getHorarioByDate(String date) {

        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(HorarioS.class);
        criteria.add(Restrictions.eq("cuadro", date));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",1),
                Restrictions.eq("evento", 5));
        criteria.add(eventos);
        criteria.addOrder(Order.asc("macro"));
        criteria.addOrder(Order.asc("linea"));
        criteria.addOrder(Order.asc("seccion"));
        criteria.addOrder(Order.asc("punto"));
        criteria.addOrder(Order.asc("instante"));
        List list = criteria.list();
        session.close();
        return list;
    }

    public List<HorarioS> getHorarioByDateIndentificador(String date, int macro, int linea, int seccion, int punto) {

        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(HorarioS.class);
        criteria.add(Restrictions.eq("cuadro", date));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",1),
                Restrictions.eq("evento", 5));
        criteria.add(eventos);
        criteria.add(Restrictions.eq("macro", macro));
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("seccion", seccion));
        criteria.add(Restrictions.eq("punto", punto));
        criteria.addOrder(Order.asc("macro"));
        criteria.addOrder(Order.asc("linea"));
        criteria.addOrder(Order.asc("seccion"));
        criteria.addOrder(Order.asc("punto"));
        criteria.addOrder(Order.asc("instante"));
        List list = criteria.list();
        session.close();
        return list;
    }


    public List<GroupedHorario> getHorarioByTipoDia(String cuadro) {

        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(HorarioS.class).setResultTransformer(Transformers.aliasToBean(GroupedHorario.class));;
        criteria.add(Restrictions.eq("cuadro", cuadro));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",1),
                Restrictions.eq("evento", 5));
        criteria.add(eventos);
        criteria.addOrder(Order.asc("macro"));
        criteria.addOrder(Order.asc("linea"));
        criteria.addOrder(Order.asc("seccion"));
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("macro"))
        .add(Projections.groupProperty("linea"))
        .add(Projections.groupProperty("seccion")));
        Object[] objects = criteria.list().toArray();

        List<GroupedHorario> listaFinal = new ArrayList<>();
        for( Object objeto : objects ){
            Object [] trs=((Object[]) objeto);
            GroupedHorario horario = new GroupedHorario();
            horario.setMacro((Integer) trs[0]);
            horario.setLinea((Integer) trs[1]);
            horario.setSeccion((Integer) trs[2]);
            listaFinal.add(horario);
        }

//        List<GroupedHorario> list = criteria.list();
        session.close();
        return listaFinal;
    }

}
