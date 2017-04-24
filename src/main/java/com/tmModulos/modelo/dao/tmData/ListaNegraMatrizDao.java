package com.tmModulos.modelo.dao.tmData;


import com.tmModulos.modelo.entity.tmData.ListaNegraMatriz;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ListaNegraMatrizDao {

    @Autowired
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addListaNegraMatriz(ListaNegraMatriz listaNegraMatriz) {
        getSessionFactory().getCurrentSession().save(listaNegraMatriz);
    }

    public void deleteListaNegraMatriz(ListaNegraMatriz listaNegraMatriz) {
        getSessionFactory().getCurrentSession().delete(listaNegraMatriz);
    }

    public void updateListaNegraMatriz(ListaNegraMatriz listaNegraMatriz) {
        getSessionFactory().getCurrentSession().update(listaNegraMatriz);
    }


    public List<ListaNegraMatriz> getListaNegraMatrizAll() {
        List list = getSessionFactory().getCurrentSession().createQuery("from  ListaNegraMatriz ").list();
        return list;
    }

}
