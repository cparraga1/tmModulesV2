package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.dao.saeBogota.GroupedHorario;
import com.tmModulos.modelo.entity.saeBogota.*;
import com.tmModulos.modelo.entity.saeBogota.HorarioS;
import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Repository
public class NodosDistanciaAuxiliarDao implements Serializable {

    private SessionFactory sessionFactoryServer = new Configuration().configure("hibernatePsql.cfg.xml").buildSessionFactory();
    Session session = null;
    Transaction transaction = null;
    Query query = null;;

    private SessionFactory sessionFactoryServerSqlServer = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    Session sessionSqlServer = null;
    Transaction transactionSqlServer = null;
    Query querySqlServer = null;;

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

    public List<Nodo> getNodo(String nombre){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(Nodo.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        List<Nodo> nodos =criteria.list();
        session.close();
        return nodos;
    }

    public void addNodo(Nodo nodo) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(nodo);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public void updateNodo(Nodo nodo) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.update(nodo);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public void addServicioDistancia(ServicioDistancia servicioDistancia) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(servicioDistancia);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public void addDistanciaNodos(DistanciaNodos distanciaNodos) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(distanciaNodos);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();

    }

    public DistanciaNodos getDistanciaNodosByServicioAndPunto(ServicioDistancia servicioDistancia, MatrizDistancia matrizDistancia, String nodoCodigo){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(DistanciaNodos.class);
        criteria.add(Restrictions.eq("servicioDistancia", servicioDistancia));
        criteria.add(Restrictions.eq("nodoCodigo", nodoCodigo));
        criteria.add(Restrictions.eq("matrizDistancia", matrizDistancia));
        DistanciaNodos distanciaNodos= (DistanciaNodos) criteria.uniqueResult();
        session.close();
        return distanciaNodos;
    }

    public ServicioDistancia getServicioDistanciaByMacroLineaSeccion(int macro, int linea, int seccion){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(ServicioDistancia.class);
        criteria.add(Restrictions.eq("macro", macro));
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("seccion", seccion));
        ServicioDistancia servicioDistancia= (ServicioDistancia) criteria.uniqueResult();
        session.close();
        return servicioDistancia;
    }


    public Secciones getSeccionesByMacroLineaAndConfig(int macro, int linea, int config, int seccion) {
        sessionSqlServer = sessionFactoryServerSqlServer.openSession();
        Criteria criteria = sessionSqlServer.createCriteria(Secciones.class);
        criteria.add(Restrictions.eq("macro",macro));
        criteria.add(Restrictions.eq("linea",linea));
        criteria.add(Restrictions.eq("config",config));
        criteria.add(Restrictions.eq("seccion",seccion));
        List<Secciones> list = criteria.list();
        sessionSqlServer.close();
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public Nodos getNodosByTipoandCode(int id, int tipo) {
        sessionSqlServer = sessionFactoryServerSqlServer.openSession();
        Criteria criteria = sessionSqlServer.createCriteria(Nodos.class);
        criteria.add(Restrictions.eq("tipo",tipo));
        criteria.add(Restrictions.eq("id",id));
        Nodos nodos = (Nodos) criteria.uniqueResult();
        sessionSqlServer.close();
        return nodos;
    }

    public List<Vigencias> getVigenciasDaoByDate(Date date) {

        sessionSqlServer = sessionFactoryServerSqlServer.openSession();
        Criteria criteria = sessionSqlServer.createCriteria(Vigencias.class);
        criteria.add(Restrictions.eq("fecha", date));
        List list = criteria.list();
        sessionSqlServer.close();
        return list;
    }

    public List<GroupedHorario> getHorarioByTipoDia(String cuadro) {

        sessionSqlServer = sessionFactoryServerSqlServer.openSession();
        Criteria criteria = sessionSqlServer.createCriteria(HorarioS.class).setResultTransformer(Transformers.aliasToBean(GroupedHorario.class));;
        criteria.add(Restrictions.eq("cuadro", cuadro));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",1),
                Restrictions.or(Restrictions.eq("evento", 5),Restrictions.eq("evento", 6)) );

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
        sessionSqlServer.close();
        return listaFinal;
    }

    public List<Lineas> getLineasByMacroAndLinea(int macro, int linea) {
        sessionSqlServer = sessionFactoryServerSqlServer.openSession();
        Criteria criteria = sessionSqlServer.createCriteria(Lineas.class);
        criteria.add(Restrictions.eq("macro",macro));
        criteria.add(Restrictions.eq("linea",linea));
        List list = criteria.list();
        sessionSqlServer.close();
        return list;
    }


    public List<NodosSeccion> getNodosSeccionesByMacroLineaAndConfig(int macro, int linea,int seccion, int config,int tipoNodo) {
        sessionSqlServer = sessionFactoryServerSqlServer.openSession();
//        int seccionAux = 3;
//        if(seccion==2){
//            seccionAux=4;
//        }
        Criteria criteria = sessionSqlServer.createCriteria(NodosSeccion.class);
//        Criterion eventos= Restrictions.or(Restrictions.eq("seccion",seccion),
//                Restrictions.eq("evento", seccionAux));
//        criteria.add(eventos);
        criteria.add(Restrictions.eq("macro",macro));
        criteria.add(Restrictions.eq("linea",linea));
        criteria.add(Restrictions.eq("seccion",seccion));
        criteria.add(Restrictions.eq("configLinea",config));
        criteria.add(Restrictions.eq("tipo",tipoNodo));
        List list = criteria.list();
        sessionSqlServer.close();
        return list;
    }

}