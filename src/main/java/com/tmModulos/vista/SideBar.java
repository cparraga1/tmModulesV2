package com.tmModulos.vista;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name="sideBar")
@SessionScoped
public class SideBar implements Serializable {

    private String page;

    @PostConstruct
    public void init() {
        this.page = "/templates/secured/tablaMaestra";

    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
