package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Equivalencias;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.jdbc.Work;
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
import java.util.List;

@Repository
@Transactional
public class EquivalenciasDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public List<Equivalencias> getEquivalenciasAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  Equivalencias ").list();
        return list;
    }

    public List<Equivalencias> getEquivalenciasByData(int linea, int sublinea, int puntoI, int puntoF){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Equivalencias.class);
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("sublinea", sublinea));
        criteria.add(Restrictions.eq("puntoInicio", puntoI));
        criteria.add(Restrictions.eq("puntoFin", puntoF));
        return criteria.list();
    }


    public void addEquivalenciasFromFile(String filename){
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = null;
        conn = sessImpl.getJDBCContext().connection();
        CopyManager copyManager = null;
        try {
            copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader(filename);
            copyManager.copyIn("COPY temp_expediciones (evento,tipo,hora_inicio,punto_inicio,hora_fin,punto_fin,dur,bus,identificador,km,inferido,noo,frec,ser_bus,des_dur,des_frec) from  STDIN DELIMITER ';' CSV HEADER encoding 'windows-1251'", fileReader );
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteEquivalencias(){
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_expediciones").executeUpdate();
    }
}
