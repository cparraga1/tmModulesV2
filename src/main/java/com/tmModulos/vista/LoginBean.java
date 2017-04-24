package com.tmModulos.vista;


import com.tmModulos.controlador.servicios.UsuarioServicios;
import com.tmModulos.controlador.servicios.Util;
import com.tmModulos.modelo.entity.tmData.Usuario;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ManagedBean(name = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private String uname;
    private String password;


    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;

    @ManagedProperty(value="#{UsuariosService}")
    private UsuarioServicios usuarioServicios;


    @PostConstruct
    public void init(){
        System.out.println("");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String loginUser() {
        boolean result = false;
        Usuario usuario = usuarioServicios.encontrarUsuarioByNombreUsuario(uname);
        if (usuario != null) {
            if (usuario.getContrasena().equals(password)) {
                HttpSession session = Util.getSession();
                session.setAttribute("user", uname);
                return navigationBean.redirectToWelcome();
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Inicio de sesion invalido",
                                "Verifique el usuario y la contraseña"));

            }
        }else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "El usuario no existe",
                            "Verifique el usuario y la contraseña"));

        }
        return navigationBean.toLogin();

    }

    public String logout() {
        HttpSession session = Util.getSession();
        session.invalidate();
        return navigationBean.toLogin();
    }

    public NavigationBean getNavigationBean() {
        return navigationBean;
    }

    public void setNavigationBean(NavigationBean navigationBean) {
        this.navigationBean = navigationBean;
    }

    public UsuarioServicios getUsuarioServicios() {
        return usuarioServicios;
    }

    public void setUsuarioServicios(UsuarioServicios usuarioServicios) {
        this.usuarioServicios = usuarioServicios;
    }
}
