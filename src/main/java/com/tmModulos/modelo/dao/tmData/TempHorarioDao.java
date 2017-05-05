package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Equivalencias;
import com.tmModulos.modelo.entity.tmData.TempHorario;
import com.tmModulos.modelo.entity.tmData.TemporalHorarios;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
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
            copyManager.copyIn("COPY temp_horario (jornada,tipo,operador,instante,serbus,evento,linea,coche,sublinea,ruta,punto,nodo,viaje)\n" +
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

    public List<TempHorario> getTablaHorarioByData(int linea, int sublinea,int ruta, int punto){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempHorario.class);
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("sublinea", sublinea));
        criteria.add(Restrictions.eq("punto", punto));
        criteria.add(Restrictions.eq("ruta", ruta));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",3),Restrictions.eq("evento", 11));

        criteria.add(eventos);
        return criteria.list();
    }
}
