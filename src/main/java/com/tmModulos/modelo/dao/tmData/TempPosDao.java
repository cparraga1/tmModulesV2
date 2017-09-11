package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.TempHorario;
import com.tmModulos.modelo.entity.tmData.TempPos;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
import java.util.List;

@Repository
@Transactional
public class TempPosDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void addTablaHorarioFromFile(String filename) throws Exception {
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = null;
        conn = sessImpl.getJDBCContext().connection();
        CopyManager copyManager = null;
        try {
            copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader(filename);
            copyManager.copyIn("COPY temp_pos (jornada,tipo,operador,inst,serbus,evento,linea,coche,sublinea,ruta,punto,nodo,viaje)\n" +
                    " FROM STDIN DELIMITER ';' CSV HEADER", fileReader );
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void addTablaHorarioTMFromFile(String filename){
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
                    " FROM STDIN DELIMITER ';' CSV HEADER", fileReader );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteTablaHorario(){
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_pos").executeUpdate();
    }

    public List<String> getTempPosNoReferenciadas(List<String> serviciosEncontrados){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempPos.class);
        criteria.add(Restrictions.not(Restrictions.in("identificador",serviciosEncontrados)));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",3),Restrictions.eq("evento", 11));
        criteria.add(eventos);
        criteria.setProjection(Projections.projectionList().add(Projections.groupProperty("identificador")));
        return criteria.list();
    }

    public List<TempPos> getTablaHorarioByData(int linea, int sublinea,int ruta, int punto){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempPos.class);
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("sublinea", sublinea));
        criteria.add(Restrictions.eq("punto", punto));
        criteria.add(Restrictions.eq("ruta", ruta));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",3),Restrictions.eq("evento", 11));
        criteria.add(eventos);
        criteria.addOrder(Order.asc("instante"));
        return criteria.list();
    }

    public Time getSumInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        Time sum = (Time) getSessionFactory().getCurrentSession().createCriteria(TempPos.class)
                .setProjection(Projections.sum("instante"))
                .add(Restrictions.between("instante", inicio,fin))
                .add(Restrictions.eq("identificador", idServicio))
                .uniqueResult();

        return sum;

    }

    public List<TempPos> getListHorario(String idServicio, Time inicio, Time fin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempPos.class);
        criteria.add(Restrictions.eq("identificador", idServicio));
        criteria.add(Restrictions.between("instante", inicio,fin));
        criteria.addOrder(Order.desc("instante"));
        return criteria.list();

    }

    public Time getMinInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        Time min = (Time) getSessionFactory().getCurrentSession().createCriteria(TempPos.class)
                .setProjection(Projections.min("instante"))
                .add(Restrictions.between("instante", inicio,fin))
                .add(Restrictions.eq("identificador", idServicio))
                .uniqueResult();

        return min;

    }

    public Time getMaxInstanteByFranjaHora(String idServicio, Time inicio, Time fin){
        Time min = (Time) getSessionFactory().getCurrentSession().createCriteria(TempPos.class)
                .setProjection(Projections.max("instante"))
                .add(Restrictions.between("instante", inicio,fin))
                .uniqueResult();

        return min;

    }
}
