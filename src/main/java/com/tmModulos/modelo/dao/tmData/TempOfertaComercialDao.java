package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.TempOfertaComercial;
import com.tmModulos.modelo.entity.tmData.TempPos;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
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
public class TempOfertaComercialDao {

    @Autowired
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addOfertaComercialFromFile(String filename,String delimiter) throws Exception {
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = null;
        conn = sessImpl.getJDBCContext().connection();
        CopyManager copyManager = null;
        try {
            copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader(filename);
            copyManager.copyIn("COPY temp_oferta_comercial (trayecto,tipo_dia, sentido, inicio_intervalo, fin_intervalo, frecuencia_objetiva, frecuencia_minima, frecuencia_maxima,capacidad_objetiva, capacidad_minima, capacidad_maxima, tipo_viaje, nodo, refuerzo)\n" +
                    " FROM STDIN DELIMITER '"+delimiter+"' CSV HEADER", fileReader );
        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        } catch (FileNotFoundException e) {
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }

    public void deleteOfertaComercial(){
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_oferta_comercial").executeUpdate();
    }

    public List<TempOfertaComercial> getOfertaComercial(int sublinea, int sentido, String tipoServicio, String nodo) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TempOfertaComercial.class);
        criteria.add(Restrictions.eq("trayecto", sublinea+""));
        criteria.add(Restrictions.eq("sentido", sentido+""));
        if(tipoServicio.equals("2")){
            criteria.add(Restrictions.eq("refuerzo", "1"));
            criteria.add(Restrictions.eq("nodo", nodo));
        }
        return criteria.list();
    }
}
