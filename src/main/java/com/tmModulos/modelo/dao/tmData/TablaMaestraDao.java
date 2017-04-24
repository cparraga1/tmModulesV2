package com.tmModulos.modelo.dao.tmData;

import com.tmModulos.modelo.entity.tmData.MatrizDistancia;
import com.tmModulos.modelo.entity.tmData.TablaMaestra;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class TablaMaestraDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addCustomer(TablaMaestra customer) {
        getSessionFactory().getCurrentSession().save(customer);
    }

    public void deleteCustomer(TablaMaestra customer) {
        getSessionFactory().getCurrentSession().delete(customer);
    }


    public void updateCustomer(TablaMaestra customer) {
        getSessionFactory().getCurrentSession().update(customer);
    }


    public TablaMaestra getCustomerById(int id) {
        List list = getSessionFactory().getCurrentSession()
                .createQuery("from TablaMaestra  where id=?")
                .setParameter(0, id).list();
        return (TablaMaestra)list.get(0);
    }

    public List<TablaMaestra> getCustomers() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  TablaMaestra").list();
        return list;
    }

    public List<TablaMaestra> getTablaMaestraByFecha(String tipoFecha, Date fecha){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TablaMaestra.class);
        criteria.add(Restrictions.eq(tipoFecha, fecha));
        return criteria.list();
    }

    public TablaMaestra getUltimaTablaMaestraByaTipoDia(String tipoDia,Date fechaCreacion){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TablaMaestra.class);
        criteria.add(Restrictions.eq("fechaCreacion", fechaCreacion));
        criteria.add(Restrictions.eq("tipoDia", tipoDia));
        criteria.addOrder(Order.desc("fechaCreacion"));
        List<TablaMaestra> list = criteria.list();
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    public List<TablaMaestra> getTablaMaestraBetwenFechas(String tipoFecha,Date fechaIni,Date fechaFin){
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(TablaMaestra.class);
        criteria.add(  Restrictions.between(tipoFecha, fechaIni, fechaFin)  );
        return criteria.list();
    }

}
