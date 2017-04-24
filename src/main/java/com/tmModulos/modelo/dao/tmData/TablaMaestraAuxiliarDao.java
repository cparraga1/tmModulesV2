package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
@Repository
public class TablaMaestraAuxiliarDao implements Serializable {

    private SessionFactory sessionFactoryServer = new Configuration().configure("hibernatePsql.cfg.xml").buildSessionFactory();
    Session session = null;
    Transaction transaction = null;
    Query query = null;;

    public SessionFactory getSessionFactoryServer() {
        return sessionFactoryServer;
    }

    public void setSessionFactoryServer(SessionFactory sessionFactoryServer) {
        this.sessionFactoryServer = sessionFactoryServer;
    }

    public GisServicio getGisServicioByTrayectoLinea(String identificador){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(GisServicio.class);
        criteria.add(Restrictions.eq("identificador", identificador));
        GisServicio gisServicio = (GisServicio) criteria.uniqueResult();
        session.close();
        return gisServicio;
    }

    public void addCicloServicio(CicloServicio cicloServicio) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(cicloServicio);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public void addVelocidadProgramada(VelocidadProgramada velocidadProgramada) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(velocidadProgramada);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public List<Horario> getHorariosByServicio(Servicio servicio){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(Horario.class);
        criteria.add(Restrictions.eq("servicio", servicio));
        List<Horario> horarios =criteria.list();
        session.close();
        return horarios;
    }

    public void addHorariosServicios(HorariosServicio horariosServicio) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(horariosServicio);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public void addTServicios(TablaMaestraServicios tablaMaestraServicios) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(tablaMaestraServicios);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public void addIntervalos(Intervalos intervalos) {
        session = sessionFactoryServer.openSession();
        Transaction tx = session.beginTransaction();
        session.save(intervalos);
        if (!tx.wasCommitted()){
            tx.commit();
        }
        session.close();
    }

    public List<ArcoTiempo> getArcoTiempoByGisCargaAndServicio(GisCarga gisCarga,GisServicio servicio){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(ArcoTiempo.class);
        criteria.add(Restrictions.eq("gisCargaArco", gisCarga));
        criteria.add(Restrictions.eq("servicio", servicio));
        List<ArcoTiempo> arcoTiempos =criteria.list();
        session.close();
        return arcoTiempos;
    }

    public Nodo getNodo(String nombre){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(Nodo.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        Nodo nodo = (Nodo) criteria.uniqueResult();
        session.close();
        return nodo;
    }

    public ServicioDistancia getServicioDistanciaByMacroLineaSeccion(int macro, int linea, int seccion){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(ServicioDistancia.class);
        criteria.add(Restrictions.eq("macro", macro));
        criteria.add(Restrictions.eq("linea", linea));
        criteria.add(Restrictions.eq("seccion", seccion));
        ServicioDistancia servicioDistancia = (ServicioDistancia) criteria.uniqueResult();
        session.close();
        return  servicioDistancia;
    }

    public DistanciaNodos getDistanciaNodosByServicioAndNodo(ServicioDistancia servicioDistancia, MatrizDistancia matrizDistancia,String nodoCodigo){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(DistanciaNodos.class);
        criteria.add(Restrictions.eq("servicioDistancia", servicioDistancia));
        criteria.add(Restrictions.eq("matrizDistancia", matrizDistancia));
        criteria.add(Restrictions.eq("nodoCodigo", nodoCodigo));

        DistanciaNodos distanciaNodos = (DistanciaNodos) criteria.uniqueResult();
        session.close();
        return distanciaNodos;
    }

    public DistanciaNodos getUltimoDistanciaNodosByServicioAndPunto(ServicioDistancia servicioDistancia, MatrizDistancia matrizDistancia){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(DistanciaNodos.class);
        criteria.add(Restrictions.eq("servicioDistancia", servicioDistancia));
        criteria.add(Restrictions.eq("matrizDistancia", matrizDistancia));
        criteria.addOrder(Order.desc("distancia"));
        List<DistanciaNodos> list = criteria.list();
        session.close();
        if( list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public TipoFranja getTipoFranjaByHorario(String horaIncio,String horaFin){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(TipoFranja.class);
        criteria.add(Restrictions.eq("horaInicio", horaIncio));
        criteria.add(Restrictions.eq("horaFin", horaFin));
        TipoFranja tipoFranja = (TipoFranja) criteria.uniqueResult();
        session.close();
        return tipoFranja;
    }

    public TipoFranja getTipoFranjaByNombre(String nombre){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(TipoFranja.class);
        criteria.add(Restrictions.eq("nombre", nombre));
        TipoFranja tipoFranja = (TipoFranja) criteria.uniqueResult();
        session.close();
        return tipoFranja;
    }

    public List<IntervalosProgramacion> getIntervaloByFranja(TipoFranja tipoFranja){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(IntervalosProgramacion.class);
        criteria.add(Restrictions.eq("tipoFranja", tipoFranja));
        List<IntervalosProgramacion> lista =criteria.list();
        session.close();
        return lista;
    }

    public List<TiempoIntervalos> getTiempoIntervalosByServicio(List<IntervalosProgramacion> intervalos, ServicioTipoDia id,GisIntervalos gisIntervalos){
        session = sessionFactoryServer.openSession();
        Criteria criteria = session.createCriteria(TiempoIntervalos.class);
        Disjunction or = Restrictions.disjunction();
        for (IntervalosProgramacion intervalo: intervalos ) {
            or.add(Restrictions.eq("intervalosProgramacion", intervalo));
        }
        criteria.add(or);
        criteria.add(Restrictions.eq("idServicio",id));
        criteria.add(Restrictions.eq("gisIntervalos",gisIntervalos));
        criteria.addOrder(Order.asc("instante"));
        List<TiempoIntervalos> lista= criteria.list();
        return lista;
    }



}
