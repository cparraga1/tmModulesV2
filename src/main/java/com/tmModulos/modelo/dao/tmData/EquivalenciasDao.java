package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.controlador.utils.ExcelUtilProcessor;
import com.tmModulos.controlador.utils.ExpDEF;
import com.tmModulos.modelo.entity.tmData.Equivalencias;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;
import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.tmModulos.controlador.utils.ExcelUtilProcessor.createCellResultados;

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
        List<Equivalencias> lista = criteria.list();
        return lista;
    }


    public void addEquivalenciasFromFile(String filename) throws Exception {
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = null;
        conn = sessImpl.getJDBCContext().connection();
        CopyManager copyManager = null;
        try {
            copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader(filename);
            copyManager.copyIn("COPY temp_expediciones (evento,tipo,hora_inicio,punto_inicio,hora_fin,punto_fin,dur,bus,identificador,km,inferido,noo,frec,ser_bus,des_dur,des_frec)" +
                    " from  STDIN DELIMITER ';' CSV HEADER", fileReader );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private void cerrarConexion(Connection conn, Session session) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEquivalencias(){
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_expediciones").executeUpdate();

    }

    public void addExpedicionesConversion(String destination,char delimiter) throws Exception {
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = null;
        conn = sessImpl.getJDBCContext().connection();
        CopyManager copyManager = null;
        try {
            copyManager = new CopyManager((BaseConnection) conn);
            FileReader fileReader = new FileReader(destination);
            copyManager.copyIn("COPY temp_expediciones_comp (evento,tipo,hora_inicio,punto_inicio,hora_fin,punto_fin,dur,bus,identificador,km,inferido,noo,frec,ser_bus,des_dur,des_frec)" +
                    " from  STDIN DELIMITER '"+delimiter+"' CSV HEADER encoding 'windows-1251'", fileReader );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    public void deleteExpedicionesConversion() {
        getSessionFactory().getCurrentSession().createSQLQuery("DELETE FROM temp_expediciones_comp").executeUpdate();
    }

    public String getNewExpediciones(String archivoReporte) {
        SessionFactory factory = getSessionFactory();
        Session session = factory.getCurrentSession();
        SessionImplementor sessImpl = (SessionImplementor) session;
        Connection conn = sessImpl.getJDBCContext().connection();

        Workbook wb = new HSSFWorkbook();
        Sheet personSheet = wb.createSheet("Exp");

        String sqlQuery = "SELECT evento,tipo,hora_inicio,punto_inicio,hora_fin,punto_fin,dur,bus,identificador," +
                "km, inferido,noo,frec,ser_bus,des_dur,des_frec FROM temp_expediciones_comp";

        try {
            PreparedStatement ps = conn.prepareStatement(sqlQuery);
            ResultSet query_set = ps.executeQuery();
            crearHeader(personSheet);
            int row = 1;
            while(query_set.next()) {
                crearContenido(personSheet,query_set,row);
                row = row + 1;
            }

            String outputDirPath = archivoReporte;
            FileOutputStream fileOut = new FileOutputStream(outputDirPath);
            wb.write(fileOut);
            fileOut.close();


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return archivoReporte;
    }

    private void crearContenido(Sheet personSheet, ResultSet query_set, int row) throws SQLException {
        Row rowInfo1 = personSheet.createRow(row);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.EVENTO+1),ExpDEF.EVENTO);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.TIPO+1),ExpDEF.TIPO);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.INICIO+1),ExpDEF.INICIO);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.DE+1),ExpDEF.DE);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.FIN+1),ExpDEF.FIN);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.A+1),ExpDEF.A);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.DUR+1),ExpDEF.DUR);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.BUS+1),ExpDEF.BUS);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.LINEA+1),ExpDEF.LINEA);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.KM+1),ExpDEF.KM);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.V_INFERIDO+1),ExpDEF.V_INFERIDO);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.ID+1),ExpDEF.ID);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.FRECUENCIA+1),ExpDEF.FRECUENCIA);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.SERVBUS+1),ExpDEF.SERVBUS);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.DESVIACION_A+1),ExpDEF.DESVIACION_A);
        createCellResultados(rowInfo1,query_set.getString(ExpDEF.DESVIACION_B+1),ExpDEF.DESVIACION_B);
    }

    private void crearHeader(Sheet personSheet) {
        Row rowInfo1 = personSheet.createRow(0);
        createCellResultados(rowInfo1,ExpDEF.EVENTO_TXT,ExpDEF.EVENTO);
        createCellResultados(rowInfo1,ExpDEF.TIPO_TXT,ExpDEF.TIPO);
        createCellResultados(rowInfo1,ExpDEF.INICIO_TXT,ExpDEF.INICIO);
        createCellResultados(rowInfo1,ExpDEF.DE_TXT,ExpDEF.DE);
        createCellResultados(rowInfo1,ExpDEF.FIN_TXT,ExpDEF.FIN);
        createCellResultados(rowInfo1,ExpDEF.A_TXT,ExpDEF.A);
        createCellResultados(rowInfo1,ExpDEF.DUR_TXT,ExpDEF.DUR);
        createCellResultados(rowInfo1,ExpDEF.BUS_TXT,ExpDEF.BUS);
        createCellResultados(rowInfo1,ExpDEF.LINEA_TXT,ExpDEF.LINEA);
        createCellResultados(rowInfo1,ExpDEF.KM_TXT,ExpDEF.KM);
        createCellResultados(rowInfo1,ExpDEF.V_INFERIDO_TXT,ExpDEF.V_INFERIDO);
        createCellResultados(rowInfo1,ExpDEF.ID_TXT,ExpDEF.ID);
        createCellResultados(rowInfo1,ExpDEF.FRECUENCIA_TXT,ExpDEF.FRECUENCIA);
        createCellResultados(rowInfo1,ExpDEF.SERVBUS_TXT,ExpDEF.SERVBUS);
        createCellResultados(rowInfo1,ExpDEF.DESVIACION_A_TXT,ExpDEF.DESVIACION_A);
        createCellResultados(rowInfo1,ExpDEF.DESVIACION_B_TXT,ExpDEF.DESVIACION_B);
    }
}
