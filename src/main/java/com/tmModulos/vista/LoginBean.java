package com.tmModulos.vista;


import com.tmModulos.controlador.servicios.UsuarioServicios;
import com.tmModulos.controlador.servicios.Util;
import com.tmModulos.modelo.entity.tmData.Role;
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
    private String nombreUsuario;
    private String password;
    private Role role;
    private String area;
    private Usuario usuario;

    //Perfil Usuario
    public boolean cambioContrasena ;
    public String contrasenaAntigua;
    public String contrasenaNueva;
    public String contrasenaNuevaRep;

    @ManagedProperty("#{MessagesView}")
    private MessagesView messagesView;

    @ManagedProperty(value="#{navigationBean}")
    private NavigationBean navigationBean;

    @ManagedProperty(value="#{UsuariosService}")
    private UsuarioServicios usuarioServicios;


    @PostConstruct
    public void init(){
        cambioContrasena = false;
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
                this.role =usuario.getRole();
                this.nombreUsuario = usuario.getNombre();
                this.area = usuario.getArea();
                this.usuario = usuario;
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
        return navigationBean.redirectToLogin();
    }

    public void cambiar(){
        if(contrasenaViejaEsCorrecta()) {
            if(contrasenasNuevasIguales()){
                usuario.setContrasena(contrasenaNueva);
                usuarioServicios.updateUsuario(usuario);
                messagesView.info(Messages.MENSAJE_CARGA_EXITOSA,Messages.ACCION_CAMBIO_PASSWORD);
            }else{
                messagesView.error(Messages.MENSAJE_CARGA_FALLIDA,Messages.ACCION_PASSWORD_NEW);
            }
        }else{
            messagesView.error(Messages.MENSAJE_CARGA_FALLIDA,Messages.ACCION_PASSWORD_OLD);
        }
    }

    private boolean contrasenasNuevasIguales() {
        if(contrasenaNueva.equals(contrasenaNuevaRep)) return true;
        return false;
    }

    private boolean contrasenaViejaEsCorrecta() {
        if(contrasenaAntigua.equals(usuario.getContrasena())) return true;

        return false;
    }

    public void modificarContrasena(){
        cambioContrasena = true;
    }

    public String gotoperfil(){
        return "/secured/miPerfil.xhtml?faces-redirect=true";
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean puedeEditar(){
        if(role!= null){
            return role.isPermisoEscribir();
        }
        return false;
    }

    public boolean puedeEliminar(){
        if(role!= null){
            return role.isPermisoEliminar();
        }
        return false;
    }

    public boolean isCambioContrasena() {
        return cambioContrasena;
    }

    public void setCambioContrasena(boolean cambioContrasena) {
        this.cambioContrasena = cambioContrasena;
    }

    public String getContrasenaAntigua() {
        return contrasenaAntigua;
    }

    public void setContrasenaAntigua(String contrasenaAntigua) {
        this.contrasenaAntigua = contrasenaAntigua;
    }

    public String getContrasenaNueva() {
        return contrasenaNueva;
    }

    public void setContrasenaNueva(String contrasenaNueva) {
        this.contrasenaNueva = contrasenaNueva;
    }

    public String getContrasenaNuevaRep() {
        return contrasenaNuevaRep;
    }

    public void setContrasenaNuevaRep(String contrasenaNuevaRep) {
        this.contrasenaNuevaRep = contrasenaNuevaRep;
    }

    public MessagesView getMessagesView() {
        return messagesView;
    }

    public void setMessagesView(MessagesView messagesView) {
        this.messagesView = messagesView;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
