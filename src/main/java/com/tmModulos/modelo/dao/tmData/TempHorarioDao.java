package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.*;
import org.hibernate.engine.SessionImplementor;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TempHorarioDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void addTablaHorarioFromFile(String filename){
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = null;
        conn = sessImpl.getJDBCContext().connection();
        CopyManager copyManager = null;
        try {
            copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader(filename);
            copyManager.copyIn("COPY temp_horario (jornada,tipo,operador,inst,serbus,evento,linea,coche,sublinea,ruta,punto,nodo,viaje)\n" +
                    " FROM STDIN DELIMITER ';' CSV HEADER encoding 'windows-1251'", fileReader );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteTablaHorario(){
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_horario").executeUpdate();
    }

    public List<TempHorario> getTablaHorarioByData(Servicio servicio, List<IntervalosProgramacion> intervalos){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempHorario.class);
       Disjunction or = Restrictions.disjunction();
        if(intervalos!=null){
            if(intervalos.size()>0){
//                for (IntervalosProgramacion intervalo: intervalos ) {
                    or.add(Restrictions.between("instante",intervalos.get(0).getInicio(),intervalos.get(intervalos.size()-1).getFin()));
//                }
                criteria.add(or);
                criteria.add(Restrictions.eq("linea", servicio.getMacro()));
                criteria.add(Restrictions.eq("sublinea", servicio.getLinea()));
                criteria.add(Restrictions.eq("punto", servicio.getPunto()));
                criteria.add(Restrictions.eq("ruta", servicio.getSeccion()));
                criteria.addOrder(Order.desc("instante"));
                Criterion eventos= Restrictions.or(Restrictions.eq("evento",3),Restrictions.eq("evento", 11));

                criteria.add(eventos);
                return criteria.list();
            }

        }


        return new ArrayList<>();
    }

    public Time getSumInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        Time sum = (Time) getSessionFactory().getCurrentSession().createCriteria(TempHorario.class)
                .setProjection(Projections.sum("instante"))
                .add(Restrictions.between("instante", inicio,fin))
                .add(Restrictions.eq("identificador", idServicio))
                .uniqueResult();

        return sum;

    }

    public List<TempHorario> getListHorario(String idServicio, Time inicio, Time fin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempHorario.class);
        criteria.add(Restrictions.eq("identificador", idServicio));
        criteria.add(Restrictions.between("instante", inicio,fin));
        criteria.addOrder(Order.desc("instante"));
        return criteria.list();

    }

    public Time getMinInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        Time min = (Time) getSessionFactory().getCurrentSession().createCriteria(TempHorario.class)
                .setProjection(Projections.min("instante"))
                .add(Restrictions.between("instante", inicio,fin))
                .add(Restrictions.eq("identificador", idServicio))
                .uniqueResult();

        return min;

    }

    public Time getMaxInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        Time min = (Time) getSessionFactory().getCurrentSession().createCriteria(TempHorario.class)
                .setProjection(Projections.max("instante"))
                .add(Restrictions.between("instante", inicio,fin))
                .uniqueResult();

        return min;

    }
}
