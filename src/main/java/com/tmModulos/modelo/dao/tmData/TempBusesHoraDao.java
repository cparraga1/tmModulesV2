package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.Servicio;
import com.tmModulos.modelo.entity.tmData.TempBusesHora;
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
public class TempBusesHoraDao {

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
            copyManager.copyIn("COPY temp_con_buses_hora (jornada,tipo,operador,inst,serbus,evento,linea,coche,sublinea,ruta,punto,nodo,viaje,ad1,ad2,ad3,ad4,ad5)\n" +
                    " FROM STDIN DELIMITER ';' CSV HEADER", fileReader );
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteTablaHorario(){
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_con_buses_hora").executeUpdate();
    }


    public List<TempBusesHora> getTablaHorarioPorServicio(Servicio servicio){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempBusesHora.class);

        criteria.add(Restrictions.eq("linea", servicio.getMacro()));
        criteria.add(Restrictions.eq("sublinea", servicio.getLinea()));
        criteria.add(Restrictions.eq("punto", servicio.getPunto()));
        criteria.add(Restrictions.eq("ruta", servicio.getSeccion()));
        criteria.addOrder(Order.asc("instante"));
        Criterion eventos= Restrictions.or(Restrictions.eq("evento",3),Restrictions.eq("evento", 11));

        criteria.add(eventos);
        return criteria.list();
    }
}
